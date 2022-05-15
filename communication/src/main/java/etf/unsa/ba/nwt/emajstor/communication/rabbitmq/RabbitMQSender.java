package etf.unsa.ba.nwt.emajstor.communication.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private String routingKeyInfo;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${routing.keyInfo}")
    public void setRoutingKeyInfo(String routingKeyInfo) {
        this.routingKeyInfo = routingKeyInfo;
    }

    @Value("${direct.exchange}")
    public void setDirectExchange(String directExchange) {
        this.directExchange = directExchange;
    }

    public void send() throws JsonProcessingException {
        logger.info("Storing notification (routing key = info, status = 1)");
        rabbitTemplate.setExchange(directExchange);
        ObjectMapper objectMapper = new ObjectMapper();
        rabbitTemplate.convertAndSend(routingKeyInfo, objectMapper.writeValueAsString(new RabbitMQStatus(1, null)));
        logger.info("Notification stored in queue sucessfully");
    }

    public void send(String uuid) throws JsonProcessingException {
        logger.info("Storing notification (routing key = info, status = 0)");
        rabbitTemplate.setExchange(directExchange);
        ObjectMapper objectMapper = new ObjectMapper();
        rabbitTemplate.convertAndSend(routingKeyInfo, objectMapper.writeValueAsString(new RabbitMQStatus(0, uuid)));
        logger.info("Notification stored in queue sucessfully");
    }
}
