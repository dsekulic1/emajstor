package etf.unsa.ba.nwt.emajstor.review.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import etf.unsa.ba.nwt.emajstor.review.repositories.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQReceiver {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final ReviewRepository reviewRepository;

    public RabbitMQReceiver(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
            RabbitMQStatus rabbitMQStatus = objectMapper.readValue(message, RabbitMQStatus.class);
            if (rabbitMQStatus.getStatus() == 0) {
                reviewRepository.deleteById(UUID.fromString(rabbitMQStatus.getId()));
            } else {
                logger.info("Async communication completed successfully.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
