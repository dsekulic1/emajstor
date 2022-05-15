package etf.unsa.ba.nwt.emajstor.communication.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import etf.unsa.ba.nwt.emajstor.communication.dto.ReviewInfo;
import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import etf.unsa.ba.nwt.emajstor.communication.model.UserModel;
import etf.unsa.ba.nwt.emajstor.communication.repositories.MessageRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.ReviewRepository;
import etf.unsa.ba.nwt.emajstor.communication.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQReceiver {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final MessageRepository messageRepository;
    private final RabbitMQSender rabbitMQSender;

    public RabbitMQReceiver(UserRepository userRepository, ReviewRepository reviewRepository, MessageRepository messageRepository, RabbitMQSender rabbitMQSender) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.messageRepository = messageRepository;
        this.rabbitMQSender = rabbitMQSender;
    }

    public void receiveMessage(String message) {
        logger.info("Received (String) " + message);
        processMessage(message);
    }

    public void receiveMessage(byte[] message) {
        String strMessage = new String(message);
        logger.info("Received (No String) " + strMessage);
        processMessage(strMessage);
    }

    private void processMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ReviewInfo reviewInfo = objectMapper.readValue(message, ReviewInfo.class);
            try {
                UserModel user = new UserModel(reviewInfo.getUser().getId(),reviewInfo.getUser().getUsername(),reviewInfo.getUser().getContactInfo().getEmail());
                UserModel worker = new UserModel(reviewInfo.getWorker().getId(),reviewInfo.getWorker().getUsername(),reviewInfo.getWorker().getContactInfo().getEmail());
                userRepository.save(user);
                userRepository.save(worker);
                Message message1 = new Message(reviewInfo.getReview().getComment(), user.getId(), worker.getId());
                messageRepository.save(message1);
                rabbitMQSender.send();
            } catch (Exception ignored) {
                rabbitMQSender.send(reviewInfo.getReview().getId().toString());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
