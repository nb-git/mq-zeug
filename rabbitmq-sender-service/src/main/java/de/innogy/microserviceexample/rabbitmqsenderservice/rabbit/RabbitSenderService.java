package de.innogy.microserviceexample.rabbitmqsenderservice.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.innogy.microserviceexample.rabbitmqsenderservice.rabbit.RabbitSenderConfig.EXCHANGE_NAME;
import static de.innogy.microserviceexample.rabbitmqsenderservice.rabbit.RabbitSenderConfig.ROUTING_KEY;

@Service
public class RabbitSenderService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendGreetingMessage(){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "Hello from RabbitSenderService");
    }
}
