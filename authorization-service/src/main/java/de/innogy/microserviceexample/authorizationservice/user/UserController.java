package de.innogy.microserviceexample.authorizationservice.user;

import de.innogy.microserviceexample.authorizationservice.hash.HashService;
import de.innogy.microserviceexample.authorizationservice.hash.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private HashService hashService;

    private UserValidationService userValidationService;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, HashService hashService, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashService = hashService;
        this.userValidationService = userValidationService;
    }

    @PostMapping("/login")
    public boolean getUser(@RequestBody Userdata userdata) {
        Userdata result = userRepository.findByUsername(userdata.getUsername());
        return userValidationService.validatePassword(result, userdata.getPassword());
    }
}
