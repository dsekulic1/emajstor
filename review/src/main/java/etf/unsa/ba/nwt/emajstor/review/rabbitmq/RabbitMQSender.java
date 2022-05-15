package etf.unsa.ba.nwt.emajstor.review.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import etf.unsa.ba.nwt.emajstor.review.dto.ReviewInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String directExchange;
    private final RabbitTemplate rabbitTemplate;
    private String routingKeySuccess;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${direct.exchange}")
    public void setDirectExchange(String directExchange) {
        this.directExchange = directExchange;
    }

    @Value("${routing.keySuccess}")
    public void setRoutingKeySuccess(String routingKeySuccess) {
        this.routingKeySuccess = routingKeySuccess;
    }

    public void send(ReviewInfo reviewInfo) throws JsonProcessingException {
        logger.info("Storing notification (routing key = success)");
        rabbitTemplate.setExchange(directExchange);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        rabbitTemplate.convertAndSend(routingKeySuccess, objectMapper.writeValueAsString(reviewInfo));
        logger.info("Notification stored in queue sucessfully");
    }
}
