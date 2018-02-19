package de.innogy.microserviceexample.rabbitmqsenderservice.rabbit.integration;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.SmartMessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;

@Configuration
@EnableRabbit
public class RabbitTestConfig {

    public static final String TESTING_EXCHANGE_NAME = "testingExchange";
    public static final String TESTING_QUEUE_NAME = "user-testing-queue";
    public static final String TESTING_ROUTING_KEY = "test.string.message";

    public String input;

    @RabbitListener(queues = TESTING_QUEUE_NAME)
    public void stringTestListener(String input){
        this.input = input;
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(TESTING_EXCHANGE_NAME);
    }

    @Bean
    public Queue stringQueue() {
        return new Queue(TESTING_QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(stringQueue()).to(appExchange()).with(TESTING_ROUTING_KEY);
    }

    @Bean
    public TestRabbitTemplate template() {
        return new TestRabbitTemplate(connectionFactory());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);
        willReturn(connection).given(factory).createConnection();
        willReturn(channel).given(connection).createChannel(anyBoolean());
        given(channel.isOpen()).willReturn(true);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() throws IOException {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        return new Jackson2JsonMessageConverter(mapper);
    }
}
