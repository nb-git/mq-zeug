package de.innogy.microserviceexample.rabbitmqreceiverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RabbitmqReceiverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqReceiverServiceApplication.class, args);
	}
}
