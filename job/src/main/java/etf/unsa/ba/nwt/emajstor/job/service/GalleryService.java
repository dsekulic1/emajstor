package etf.unsa.ba.nwt.emajstor.job.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.model.Gallery;
import etf.unsa.ba.nwt.emajstor.job.repositories.GalleryRepository;
import etf.unsa.ba.nwt.emajstor.systemevents.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.systemevents.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.systemevents.event.EventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryRepository galleryRepository;
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

    public List<Gallery> getAllGallery() {
        registerEvent(EventRequest.actionType.GET, "/api/gallery/all", "200");
        return galleryRepository.findAll();
    }

    public Gallery addGallery(Gallery gallery) {
        if (gallery == null) {
            registerEvent(EventRequest.actionType.CREATE, "/api/gallery", "400");
            throw new BadRequestException("Gallery is empty.");
        }
        return galleryRepository.save(gallery);
    }

    public Gallery getGalleryById(UUID id) {
        Optional<Gallery> optionalGallery = galleryRepository.findById(id);
        if (optionalGallery.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/gallery/{id}", "200");
            return optionalGallery.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/gallery/{id}", "404");
            throw new NotFoundException("Gallery with id " + id.toString() + " does not exist.");
        }
    }

    public Gallery updateGalleryById(Gallery gallery, UUID id) {
        if (!galleryRepository.existsById(id)) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/gallery/{id}", "404");
            throw new NotFoundException("Gallery with id " + id.toString() + " does not exist.");
        }
        registerEvent(EventRequest.actionType.UPDATE, "/api/gallery/{id}", "200");
        return galleryRepository.save(gallery);
    }

    public Gallery deleteGalleryById(UUID id) {
        Gallery gallery = getGalleryById(id);
        if (gallery == null) {
            registerEvent(EventRequest.actionType.DELETE, "/api/gallery/{id}", "404");
            throw new NotFoundException("Gallery with id " + id.toString() + " does not exist.");
        }
        registerEvent(EventRequest.actionType.DELETE, "/api/gallery/{id}", "200");
        galleryRepository.deleteById(id);
        return gallery;
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
