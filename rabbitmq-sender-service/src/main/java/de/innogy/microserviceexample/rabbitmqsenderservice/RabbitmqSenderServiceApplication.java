package de.innogy.microserviceexample.rabbitmqsenderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RabbitmqSenderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqSenderServiceApplication.class, args);
	}

}
