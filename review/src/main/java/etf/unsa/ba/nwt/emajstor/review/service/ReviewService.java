package etf.unsa.ba.nwt.emajstor.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.review.dto.Email;
import etf.unsa.ba.nwt.emajstor.review.dto.Message;
import etf.unsa.ba.nwt.emajstor.review.dto.ReviewInfo;
import etf.unsa.ba.nwt.emajstor.review.dto.User;
import etf.unsa.ba.nwt.emajstor.review.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.review.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.review.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.review.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.review.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.review.model.Review;
import etf.unsa.ba.nwt.emajstor.review.rabbitmq.RabbitMQSender;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate;
    private final RabbitMQSender rabbitMQSender;

    private static String grpcUrl;
    private static int grpcPort;

    public ReviewService(ReviewRepository reviewRepository, RestTemplate restTemplate, RabbitMQSender rabbitMQSender) {
        this.reviewRepository = reviewRepository;
        this.restTemplate = restTemplate;
        this.rabbitMQSender = rabbitMQSender;
    }

    @Value("${app.grpc-url}")
    public void setGrpcUrl(String grpcUrl) {
        this.grpcUrl = grpcUrl;
    }

    @Value("${app.grpc-port}")
    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }


    public List<Review> getAllReviews() {
        registerEvent(EventRequest.actionType.GET, "/api/review/all", "200");
        return reviewRepository.findAll();
    }

    public Review getReviewById(UUID id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/review/{id}", "200");
            return optionalReview.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/review/{id}", "400");
            throw new BadRequestException("Review with id " + id + " does not exist.");
        }
    }

    public Review deleteReviewById(UUID id) {
        Review review = getReviewById(id);
        if (review == null) {
            registerEvent(EventRequest.actionType.DELETE, "/api/review/{id}", "404");
            throw new NotFoundException("Review does not exist.");
        }
        reviewRepository.deleteById(id);
        registerEvent(EventRequest.actionType.DELETE, "/api/review/{id}", "200");
        return review;
    }

    public Review addReview(Review review) throws ServiceUnavailableException {
            if (validateReview(review)) {
                registerEvent(EventRequest.actionType.CREATE, "/api/review", "400");
                throw new BadRequestException("Review comment must contains text.");
            }

            try {
                User user = getUser(review.getUser());
                User worker = getUser(review.getWorker());
                Review savedReview = reviewRepository.save(review);
                ///api/v1/email/send/simple
                JSONObject body = new JSONObject();
                body.put("to", worker.getContactInfo().getEmail());
                body.put("from", "slanjeobavijesti@gmail.com");
                body.put("subject", "Novi review");
                String text = "Poštovani "+ worker.getContactInfo().getFirstName() +",\n\n" +"Novi review je: "+review.getComment()+" i ocjena je: "+review.getNumStars()+".\n\nVaš eMjstor team";
                body.put("text", text);


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
                restTemplate.postForObject(
                        "http://communication/api/v1/email/send/simple",
                        entity,
                        Email.class
                );

                try {
                    rabbitMQSender.send(new ReviewInfo(user, worker, savedReview));
                } catch (JsonProcessingException exception) {
                    registerEvent(EventRequest.actionType.CREATE, "/api/review", "500");
                    reviewRepository.deleteById(savedReview.getId());
                    throw exception;
                }

                registerEvent(EventRequest.actionType.CREATE, "/api/review", "200");
                return savedReview;
            } catch (Exception exception) {
                registerEvent(EventRequest.actionType.CREATE, "/api/review", "404");
                throw new NotFoundException("User does not exist.");
            }
    }

    public Review updateReviewById(Review review, UUID id) {
        if(review == null) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/review/{id}", "400");
            throw new BadRequestException("Wrong review.");
        }
        if (reviewRepository.findById(id).isEmpty()) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/review/{id}", "404");
            throw new NotFoundException("Review with id " + id + " does not exist.");
        }
        registerEvent(EventRequest.actionType.UPDATE, "/api/review/{id}", "200");
        return reviewRepository.save(review);
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

    private Boolean validateReview (Review review) {
        return (StringUtils.isEmpty(review.getComment()) || StringUtils.isBlank(review.getComment()));
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
                    .setMicroservice("Review service")
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
