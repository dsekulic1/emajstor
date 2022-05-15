package etf.unsa.ba.nwt.emajstor.job.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.job.dto.User;
import etf.unsa.ba.nwt.emajstor.job.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.job.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.job.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.repositories.JobRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    private static String grpcUrl;
    private static int grpcPort;

    public JobService(JobRepository jobRepository, RestTemplate restTemplate) {
        this.jobRepository = jobRepository;
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

    public List<Job> getAllJobs() {
        registerEvent(EventRequest.actionType.GET, "/api/job/all", "200");
        return jobRepository.findAll();
    }

    public Job addJob(Job job) {
        if (job == null) {
            registerEvent(EventRequest.actionType.CREATE, "/api/job", "400");
            throw new BadRequestException("Job is empty.");
        }
        registerEvent(EventRequest.actionType.CREATE, "/api/job", "200");
        return jobRepository.save(job);
    }

    public Job getJobById(UUID id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/job/{id}", "200");
            return optionalJob.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/job/{id}", "404");
            throw new NotFoundException("Job with id " + id.toString() + " does not exist.");
        }
    }

    public Job updateJobById(Job job, UUID id) {
        if (!jobRepository.existsById(id)) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/job/{id}", "404");
            throw new BadRequestException("Job with id " + id.toString() + " does not exist.");
        }
        registerEvent(EventRequest.actionType.UPDATE, "/api/job/{id}", "200");
        return jobRepository.save(job);
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

    public class EmployeeList {
        private List<User> users;

        public EmployeeList() {
            users = new ArrayList<>();
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

    }

}
