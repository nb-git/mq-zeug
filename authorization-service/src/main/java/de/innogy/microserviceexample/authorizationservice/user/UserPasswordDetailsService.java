package de.innogy.microserviceexample.authorizationservice.user;

import de.innogy.microserviceexample.authorizationservice.hash.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordDetailsService implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);

    private UserRepository userRepository;
    private UserValidationService userValidationService;


    public UserPasswordDetailsService(UserRepository userRepository, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    //FIXME
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Authenticate user: " + username);
        return userRepository.findByUsername(username);

        return null;
    }


}
