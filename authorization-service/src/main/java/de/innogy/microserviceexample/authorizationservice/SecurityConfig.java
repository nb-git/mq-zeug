package de.innogy.microserviceexample.authorizationservice;

import de.innogy.microserviceexample.authorizationservice.user.UserPasswordDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private UserPasswordDetailsService userPasswordDetailsService;

    @Autowired
    public SecurityConfig(UserPasswordDetailsService userPasswordDetailsService) {
        this.userPasswordDetailsService = userPasswordDetailsService;
    }


}
