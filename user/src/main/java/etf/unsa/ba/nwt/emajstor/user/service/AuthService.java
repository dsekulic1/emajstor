package etf.unsa.ba.nwt.emajstor.user.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.user.dto.Email;
import etf.unsa.ba.nwt.emajstor.user.dto.RegisterDTO;
import etf.unsa.ba.nwt.emajstor.user.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.user.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.user.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.user.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.user.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import etf.unsa.ba.nwt.emajstor.user.model.Role;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.repositories.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repositories.UserRepository;
import etf.unsa.ba.nwt.emajstor.user.request.LoginRequest;
import etf.unsa.ba.nwt.emajstor.user.request.SignupRequest;
import etf.unsa.ba.nwt.emajstor.user.request.UpdateRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactInfoRepository contactInfoRepository;
    private final RestTemplate restTemplate;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ContactInfoRepository contactInfoRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contactInfoRepository = contactInfoRepository;
        this.restTemplate = restTemplate;
    }

    public User signup(SignupRequest signupRequest) throws JSONException {
        if (userRepository.existsByUsernameIgnoreCase(signupRequest.getUsername())) {
            registerEvent(EventRequest.actionType.CREATE, "/api/auth/signup", "409");

            throw new ConflictException("Username is already taken");
        }
        if (contactInfoRepository.existsByEmail(signupRequest.getEmail())) {
            registerEvent(EventRequest.actionType.CREATE, "/api/auth/signup", "409");

            throw new ConflictException("Email is already taken");
        }
        ContactInfo ci = new ContactInfo(
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getEmail(),
                signupRequest.getNumber()
        );
        ContactInfo contactInfo = contactInfoRepository.save(ci);
        User p = new User(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getCity(),
                signupRequest.getLocationLongitude(),
                signupRequest.getLocationLatitude(),
                LocalDateTime.now(),
                contactInfo
                );
        // postavka role
        if (signupRequest.getWorker()) {
            p.setRole(Role.ROLE_WORKER);
        } else {
            p.setRole(Role.ROLE_USER);
        }
        User user = userRepository.save(p);
        registerEvent(EventRequest.actionType.CREATE, "/api/auth/signup", "200");
        sendEmail(user.getContactInfo().getEmail(), user.getContactInfo().getFirstName()+" " +user.getContactInfo().getLastName());
        return user;
    }

    public User updateProfile(UpdateRequest updateProfileRequest) {
        User user = userRepository.findByUsername(updateProfileRequest.getUsername())
                .orElseThrow(() -> {
                    registerEvent(EventRequest.actionType.UPDATE, "/api/auth/update-profile", "400");
                    throw  new BadRequestException("Username doesn't exist");
                });

        Optional<ContactInfo> optionalContactInfo = contactInfoRepository.findByEmail(updateProfileRequest.getEmail());

        if (optionalContactInfo.isPresent() && !optionalContactInfo.get().getId().equals(user.getContactInfo().getId())) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/auth/update-profile", "409");
            throw new ConflictException("Email is already taken");
        }

        user.getContactInfo().setFirstName(updateProfileRequest.getFirstName());
        user.getContactInfo().setLastName(updateProfileRequest.getLastName());
        user.getContactInfo().setEmail(updateProfileRequest.getEmail());
        user.setCity(updateProfileRequest.getCity());
        user.setLocationLatitude(updateProfileRequest.getLocationLatitude());
        user.setLocationLongitude(updateProfileRequest.getLocationLongitude());
        user.getContactInfo().setNumber(updateProfileRequest.getNumber());
        registerEvent(EventRequest.actionType.UPDATE, "/api/auth/update-profile", "200");

        return user;
    }

    private void sendEmail(String email, String name) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("name", name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        restTemplate.postForObject(
                "http://communication/api/v1/email/send/register",
                entity,
                RegisterDTO.class
        );
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    registerEvent(EventRequest.actionType.CREATE, "/api/auth/login", "401");
                    throw new UsernameNotFoundException("User not found with username: " + loginRequest.getUsername());
                } );
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            registerEvent(EventRequest.actionType.CREATE, "/api/auth/login", "401");
            throw new UsernameNotFoundException("Wrong username or password");
        }
        registerEvent(EventRequest.actionType.CREATE, "/api/auth/login", "200");
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
