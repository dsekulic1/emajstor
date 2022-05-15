package etf.unsa.ba.nwt.emajstor.review.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:beans.xml")
@Configuration
public class RabbitMQConfig {
    private static final String LISTENER_METHOD = "receiveMessage";
    private String queueName;
    private String directExchange;
    private String routingKeyInfo;

    @Value("${queue.name}")
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Value("${direct.exchange}")
    public void setDirectExchange(String directExchange) {
        this.directExchange = directExchange;
    }

    @Value("${routing.keyInfo}")
    public void setRoutingKeyInfo(String routingKeyInfo) {
        this.routingKeyInfo = routingKeyInfo;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(directExchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyInfo);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitMQReceiver receiver) {
        return new MessageListenerAdapter(receiver, LISTENER_METHOD);
    }
}
