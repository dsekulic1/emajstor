package etf.unsa.ba.nwt.emajstor.communication.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.communication.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.communication.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.communication.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.communication.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.communication.model.ChatMessage;
import etf.unsa.ba.nwt.emajstor.communication.repositories.ChatMessageRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private static String grpcUrl;
    private static int grpcPort;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Value("${app.grpc-url}")
    public void setGrpcUrl(String grpcUrl) {
        this.grpcUrl = grpcUrl;
    }

    @Value("${app.grpc-port}")
    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    public ChatMessage addChatMessage(final ChatMessage chatMessage) {
        if (chatMessage == null) {
            registerEvent(EventRequest.actionType.CREATE, "/api/chat/message", "400");
            throw new BadRequestException("Message must contains text.");
        }

        try {
            ChatMessage newMessage = chatMessageRepository.save(chatMessage);
            registerEvent(EventRequest.actionType.CREATE, "/api/chat/message", "200");
            return newMessage;
        } catch (Exception exception) {
            throw exception;
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
                    .setMicroservice("Communication service")
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
