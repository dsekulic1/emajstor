package etf.unsa.ba.nwt.emajstor.job.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.job.dto.User;
import etf.unsa.ba.nwt.emajstor.job.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.job.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.job.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.repositories.DealRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DealService {

    private final DealRepository dealRepository;
    private final RestTemplate restTemplate;
    private static String grpcUrl;
    private static int grpcPort;

    public DealService(DealRepository dealRepository, RestTemplate restTemplate) {
        this.dealRepository = dealRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${app.grpc-url}")
    public void setGrpcUrl(String grpcUrl) {
        this.grpcUrl = grpcUrl;
    }

    @Value("${app.grpc-port}")
    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    public List<Deal> getAllDeals() {
        registerEvent(EventRequest.actionType.GET, "/api/deal/all", "200");
        return dealRepository.findAll();
    }

    public Deal addDeal(Deal deal) {

        try {
            if (getUser(deal.getUser()) != null || getUser(deal.getJob().getUser()) != null) {
                registerEvent(EventRequest.actionType.CREATE, "/api/deal", "200");
                return dealRepository.save(deal);
            } else {
                registerEvent(EventRequest.actionType.CREATE, "/api/deal", "404");
                throw new NotFoundException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            registerEvent(EventRequest.actionType.CREATE, "/api/deal", "404");
            throw new NotFoundException("User does not exist.");
        }
    }

    public Deal getDealById(UUID id) {
        Optional<Deal> optionalDeal = dealRepository.findById(id);
        if (optionalDeal.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/deal/{id}", "200");
            return optionalDeal.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/deal/{id}", "404");
            throw new NotFoundException("Deal with id " + id.toString() + " does not exist.");
        }
    }

    public Deal updateDealById(Deal deal, UUID id) {
        if (!dealRepository.existsById(id)) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/deal/{id}", "404");
            throw new NotFoundException("Deal with id " + id.toString() + " does not exist.");
        }

        try {
            if (getUser(deal.getUser()) != null || getUser(deal.getJob().getUser()) != null) {
                registerEvent(EventRequest.actionType.UPDATE, "/api/deal/{id}", "200");
                return dealRepository.save(deal);
            } else {
                registerEvent(EventRequest.actionType.UPDATE, "/api/deal/{id}", "400");
                throw new BadRequestException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/deal/{id}", "404");
            throw new NotFoundException("User does not exist.");
        }
    }

    public boolean resolveDeal(final UUID id) {
        Deal deal = getDealById(id);
        deal.setFinished(true);
        return dealRepository.save(deal).getFinished();
    }

    public User getUser(final UUID id) throws ServiceUnavailableException {
        try {
            return restTemplate.getForObject(
                    "http://user/api/users/{id}",
                    User.class,
                    id
            );
        } catch (ResourceAccessException ex) {
            registerEvent(EventRequest.actionType.GET, "/user/api/user", "503");
            throw new ServiceUnavailableException("Error while communicating with another microservice.");
        }
    }

    private void registerEvent(EventRequest.actionType actionType, String resource, String status) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcUrl, grpcPort)
                .usePlaintext()
                .build();

        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);

        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();

        try {
            EventResponse eventResponse = stub.log(EventRequest.newBuilder()
                    .setDate(timestamp)
                    .setMicroservice("Job service")
                    .setUser("Unknown")
                    .setAction(actionType)
                    .setResource(resource)
                    .setStatus(status)
                    .build());
        } catch (StatusRuntimeException e) {
            System.out.println("System event microservice not running");
        }

        channel.shutdown();
    }
}
