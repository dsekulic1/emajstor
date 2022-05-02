package etf.unsa.ba.nwt.emajstor.user.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.user.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.user.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.user.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.user.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.user.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.user.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.repositories.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repositories.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;
    private static String grpcUrl;
    private static int grpcPort;

    public UserService(UserRepository userRepository, ContactInfoRepository contactInfoRepository) {
        this.userRepository = userRepository;
        this.contactInfoRepository = contactInfoRepository;
    }

    @Value("${app.grpc-url}")
    public void setGrpcUrl(String grpcUrl) {
        this.grpcUrl = grpcUrl;
    }

    @Value("${app.grpc-port}")
    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    public List<User> getAllUsers() {
        registerEvent(EventRequest.actionType.GET, "/api/user/all", "200");
        return userRepository.findAll();
    }

    public User addUser(User user) {
        try {
            ContactInfo contactInfo = user.getContactInfo();
            if (contactInfo == null) {
                registerEvent(EventRequest.actionType.CREATE, "/api/user", "400");
                throw new BadRequestException("Contact info can not be null.");
            }

            if (contactInfoRepository.existsByEmail(contactInfo.getEmail())) {
                registerEvent(EventRequest.actionType.CREATE, "/api/user", "409");
                throw new ConflictException("Email already taken.");
            } else if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
                registerEvent(EventRequest.actionType.CREATE, "/api/user", "409");
                throw new ConflictException("Username already taken.");
            } else {
               contactInfo = contactInfoRepository.save(contactInfo);
               user.setContactInfo(contactInfo);
               registerEvent(EventRequest.actionType.CREATE, "/api/user", "200");
               return userRepository.save(user);
            }
        } catch (ConflictException exception) {
            throw exception;
        }
    }

    public User getUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/user/{id}", "200");
            return optionalUser.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/user/{id}", "400");
            throw new BadRequestException("User with id " + id.toString() + " does not exist.");
        }
    }

    public User getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/user/{username}", "200");
            return optionalUser.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/user/{username}", "400");
            throw new BadRequestException("User with username " + username + " does not exist.");
        }
    }

    public User updateUserById(User user, UUID id) {
        if (userRepository.findById(id).isEmpty()) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/user/{id}", "404");
            throw new NotFoundException("User with id " + id + " does not exist.");
        } else {
            try {
                ContactInfo contactInfo = contactInfoRepository.save(user.getContactInfo());
                user.setContactInfo(contactInfo);
                registerEvent(EventRequest.actionType.UPDATE, "/api/user/{id}", "200");
                return userRepository.save(user);
            } catch (Exception exception) {
                registerEvent(EventRequest.actionType.UPDATE, "/api/user/{id}", "400");
                throw new BadRequestException("Email or username already exists. Please change it");
            }
        }
    }

    public User deleteUserById(UUID id) {
        User user = getUserById(id);

        if(user != null) {
            userRepository.deleteById(user.getId());
            contactInfoRepository.delete(user.getContactInfo());
            registerEvent(EventRequest.actionType.UPDATE, "/api/user/{id}", "200");
        } else {
            registerEvent(EventRequest.actionType.DELETE, "/api/user/{id}", "400");
            throw new BadRequestException("User does not exist.");
        }

        return user;
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
                    .setMicroservice("User service")
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
