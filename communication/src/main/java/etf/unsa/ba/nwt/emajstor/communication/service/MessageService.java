package etf.unsa.ba.nwt.emajstor.communication.service;

import com.google.protobuf.Timestamp;
import etf.unsa.ba.nwt.emajstor.communication.dto.User;
import etf.unsa.ba.nwt.emajstor.communication.event.EventRequest;
import etf.unsa.ba.nwt.emajstor.communication.event.EventResponse;
import etf.unsa.ba.nwt.emajstor.communication.event.EventServiceGrpc;
import etf.unsa.ba.nwt.emajstor.communication.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.communication.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import etf.unsa.ba.nwt.emajstor.communication.model.NotificationHistory;
import etf.unsa.ba.nwt.emajstor.communication.repositories.MessageRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.NotificationHistoryRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import javax.mail.MessagingException;
import javax.naming.ServiceUnavailableException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private static String grpcUrl;
    private static int grpcPort;

    public MessageService(MessageRepository messageRepository, NotificationHistoryRepository notificationHistoryRepository, EmailService emailService, RestTemplate restTemplate) {
        this.messageRepository = messageRepository;
        this.notificationHistoryRepository = notificationHistoryRepository;
        this.emailService = emailService;
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

    public List<Message> getAllMessages() {
        registerEvent(EventRequest.actionType.GET, "/api/messages/all", "200");
        return messageRepository.findAll();
    }

    public Message addMessage(Message message) throws ServiceUnavailableException, MessagingException {
        if (validateMessage(message)) {
            registerEvent(EventRequest.actionType.CREATE, "/api/messages", "400");
            throw new BadRequestException("Message must contains text.");
        }

        try {
           Message newMessage = messageRepository.save(message);
           NotificationHistory notificationHistory = new NotificationHistory();
           notificationHistory.setMessage(newMessage);
           notificationHistory.setUser(newMessage.getReceiver());
           notificationHistory.setTimeStamp(LocalDateTime.now());
           User sender = getUser(message.getSender());
           User receiver = getUser(message.getReceiver());
           //emailService.sendNotification(sender, receiver);
           notificationHistoryRepository.save(notificationHistory);
           registerEvent(EventRequest.actionType.CREATE, "/api/messages", "200");
           return newMessage;
        } catch (Exception exception) {
            throw exception;
        }
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

    public Message getMessageById(UUID id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/{id}", "200");
            return optionalMessage.get();
        } else {
            registerEvent(EventRequest.actionType.GET, "/api/messages/{id}", "400");
            throw new BadRequestException("Message with id " + id + " does not exist.");
        }
    }

    public List<Message> findAllByReceiver(UUID receiver) {
        if (receiver == null) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/receiver/{id}", "400");
            throw new BadRequestException("Please provide receiver id.");
        }

        List<Message> messageList = messageRepository.findAllByReceiver(receiver);

        if (messageList.isEmpty()) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/receiver/{id}", "404");
            throw new NotFoundException("Message for the receiver with id = " + receiver + " does not found.");
        }

        registerEvent(EventRequest.actionType.GET, "/api/messages/receiver/{id}", "200");
        return messageList;
    }

    public List<Message> findAllBySender(UUID sender) {
        if (sender == null) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/sender/{id}", "400");
            throw new BadRequestException("Please provide sender id.");
        }

        List<Message> messageList = messageRepository.findAllBySender(sender);

        if (messageList.isEmpty()) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/sender/{id}", "404");
            throw new NotFoundException("Message for the sender with id = " + sender + " does not found.");
        }

        registerEvent(EventRequest.actionType.GET, "/api/messages/sender/{id}", "200");
        return messageList;
    }

    public Message updateMessageById(Message message, UUID id) {
        if (messageRepository.findById(id).isEmpty()) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/messages/{id}", "404");
            throw new NotFoundException("Message with id " + id + " does not exist.");
        }

        try {
            if (getUser(message.getSender()) != null || getUser(message.getReceiver()) != null) {
                registerEvent(EventRequest.actionType.UPDATE, "/api/messages/{id}", "200");
                return messageRepository.save(message);
            } else {
                registerEvent(EventRequest.actionType.UPDATE, "/api/messages/{id}", "400");
                throw new BadRequestException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            registerEvent(EventRequest.actionType.UPDATE, "/api/messages/{id}", "400");
            throw new BadRequestException("User or worker does not exist.");
        }

    }

    public Message deleteMessageById(UUID id){
        Message message = getMessageById(id);
        if (message == null) {
            registerEvent(EventRequest.actionType.DELETE, "/api/messages", "404");
            throw new NotFoundException("Message does not exist.");
        }
        messageRepository.deleteById(id);
        notificationHistoryRepository.deleteByMessage(message);
        registerEvent(EventRequest.actionType.DELETE, "/api/messages", "200");
        return message;
    }

    private Boolean validateMessage (Message message) {
        return (StringUtils.isEmpty(message.getText()) || StringUtils.isBlank(message.getText()));
    }


    public List<Message> findAllMessageByText(String textMessage) {
        if (textMessage == null) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/text/{textMessage}", "400");
            throw new BadRequestException("Please insert text of message.");
        }

        List<Message> messageList = messageRepository.findAll();
        List<Message> returnMessages = new ArrayList<>();

        for (Message message : messageList){
            if(message.getText().contains(textMessage))
                returnMessages.add(message);
        }

        if (returnMessages.isEmpty()) {
            registerEvent(EventRequest.actionType.GET, "/api/messages/text/{textMessage}", "404");
            throw new NotFoundException("Message with this text is not found.");
        }

        registerEvent(EventRequest.actionType.GET, "/api/messages/text/{textMessage}", "200");
        return returnMessages;
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
