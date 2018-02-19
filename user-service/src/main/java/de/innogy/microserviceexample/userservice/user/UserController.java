package de.innogy.microserviceexample.userservice.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/greet")
    public String greet(){
        return "Hello";
    }
}
