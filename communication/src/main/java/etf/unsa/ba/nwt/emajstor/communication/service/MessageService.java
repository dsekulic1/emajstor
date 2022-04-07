package etf.unsa.ba.nwt.emajstor.communication.service;

import etf.unsa.ba.nwt.emajstor.communication.dto.User;
import etf.unsa.ba.nwt.emajstor.communication.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.communication.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import etf.unsa.ba.nwt.emajstor.communication.model.NotificationHistory;
import etf.unsa.ba.nwt.emajstor.communication.repositories.MessageRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import javax.mail.MessagingException;
import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final EmailService emailService;
    private final RestTemplate restTemplate;

    public List<Message> getAllMessages() { return  messageRepository.findAll(); }

    public Message addMessage(Message message) throws ServiceUnavailableException, MessagingException {
        if (validateMessage(message)) {
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
           emailService.sendNotification(sender, receiver);
           notificationHistoryRepository.save(notificationHistory);
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
            throw new ServiceUnavailableException("Error while communicating with another microservice.");
        }
    }

    public Message getMessageById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Message with id " + id + " does not exist."));
    }

    public List<Message> findAllByReceiver(UUID receiver) {
        if (receiver == null) {
            throw new BadRequestException("Please provide receiver id.");
        }

        List<Message> messageList = messageRepository.findAllByReceiver(receiver);

        if (messageList.isEmpty()) {
            throw new NotFoundException("Message for the receiver with id = " + receiver + " does not found.");
        }

        return messageList;
    }

    public List<Message> findAllBySender(UUID sender) {
        if (sender == null) {
            throw new BadRequestException("Please provide sender id.");
        }

        List<Message> messageList = messageRepository.findAllBySender(sender);

        if (messageList.isEmpty()) {
            throw new NotFoundException("Message for the sender with id = " + sender + " does not found.");
        }

        return messageList;
    }

    public Message updateMessageById(Message message, UUID id) {
        if (messageRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Message with id " + id + " does not exist.");
        }

        try {
            if (getUser(message.getSender()) != null || getUser(message.getReceiver()) != null) {
                return messageRepository.save(message);
            } else {
                throw new BadRequestException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            throw new BadRequestException("User or worker does not exist.");
        }

    }

    public Message deleteMessageById(UUID id){
        Message message = getMessageById(id);

        messageRepository.deleteById(id);
        notificationHistoryRepository.deleteByMessage(message);

        return message;
    }

    private Boolean validateMessage (Message message) {
        return (StringUtils.isEmpty(message.getText()) || StringUtils.isBlank(message.getText()));
    }


    public List<Message> findAllMessageByText(String textMessage) {
        if (textMessage == null) {
            throw new BadRequestException("Please insert text of message.");
        }

        List<Message> messageList = messageRepository.findAll();
        List<Message> returnMessages = new ArrayList<>();

        for (Message message : messageList){
            if(message.getText().contains(textMessage))
                returnMessages.add(message);
        }

        if (returnMessages.isEmpty()) {
            throw new NotFoundException("Message with this text is not found.");
        }

        return returnMessages;
    }
}
