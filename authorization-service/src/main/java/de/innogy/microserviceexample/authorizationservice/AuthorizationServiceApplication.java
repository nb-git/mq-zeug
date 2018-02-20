package de.innogy.microserviceexample.authorizationservice;

import de.innogy.microserviceexample.authorizationservice.hash.HashService;
import de.innogy.microserviceexample.token.TokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthorizationServiceApplication {

	@Autowired
	HashService hashService;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}

	@Bean
    @ConfigurationProperties(prefix = "security.token")
    public TokenProperties tokenProperties(){
	    return new TokenProperties();
    }


}
