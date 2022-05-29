package etf.unsa.ba.nwt.emajstor.job.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.job.dto.User;
import etf.unsa.ba.nwt.emajstor.job.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.job.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.job.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.repositories.BusinessRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;

    private static String grpcUrl;
    private static int grpcPort;

    @Value("${app.grpc-url}")
    public void setGrpcUrl(String grpcUrl) {
        this.grpcUrl = grpcUrl;
    }

    @Value("${app.grpc-port}")
    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    public List<Business> getAllBusiness() {
        registerEvent(EventRequest.actionType.GET, "/api/business/all", "200");
        return businessRepository.findAll();
    }

    public Business addBusiness(Business business) {
        try {
            if (businessRepository.existsByNameIgnoreCase(business.getName())) {
                registerEvent(EventRequest.actionType.CREATE, "/api/business", "409");
                throw new ConflictException("Name already taken.");
            }
            registerEvent(EventRequest.actionType.CREATE, "/api/business", "200");
            return businessRepository.save(business);
        } catch (ConflictException conflictException) {
            registerEvent(EventRequest.actionType.CREATE, "/api/business", "409");
            throw conflictException;
        }
    }

    public Business addOrGetBusiness(String business) {
        Optional<Business> optionalBusiness = businessRepository.findByName(business);
        if (optionalBusiness.isPresent()) {
            return optionalBusiness.get();
        }
        Business business1 = new Business(business);
        return businessRepository.save(business1);
    }

    public Business getBusinessByName(String name) {
        Optional<Business> optionalBusiness = businessRepository.findByName(name);
        if (optionalBusiness.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/business/name/{name}", "200");
            return optionalBusiness.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/business/name/{name}", "400");
            throw new BadRequestException("Business with name " + name + " does not exist.");
        }
    }

    public Business getBusinessById(UUID id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/business/{id}", "200");
            return optionalBusiness.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/business/{id}", "400");
            throw new BadRequestException("Business with id " + id + " does not exist.");
        }
    }

    public Business updateBusinessById(Business business, UUID id) {
        if(!businessRepository.existsById(id)) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/business/{id}", "404");
            throw new NotFoundException("Business with id " + id + " does not exist.");
         } else if (businessRepository.existsByNameIgnoreCase(business.getName())) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/business/{id}", "400");
            throw new BadRequestException("Business with name " + business.getName() + " already exist.");
         } else {
            registerEvent(EventRequest.actionType.UPDATE, "/api/business/{id}", "200");
            return businessRepository.save(business);
         }
    }

    public Business deleteBusinessById(UUID id) {
        Business business = getBusinessById(id);
        if (business == null) {
            registerEvent(EventRequest.actionType.DELETE, "/api/business/{id}", "404");
            throw new NotFoundException("Business does not exist.");
        }
        businessRepository.deleteById(id);
        registerEvent(EventRequest.actionType.DELETE, "/api/business/{id}", "200");
        return business;
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
