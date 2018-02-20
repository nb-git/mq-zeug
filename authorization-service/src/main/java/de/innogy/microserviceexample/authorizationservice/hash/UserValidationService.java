package de.innogy.microserviceexample.authorizationservice.hash;

import de.innogy.microserviceexample.authorizationservice.user.Userdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Service to validate a users password.
 */
@Service
public class UserValidationService {

    Logger LOGGER = LoggerFactory.getLogger(UserValidationService.class);

    private HashService hashService;

    public UserValidationService(HashService hashService) {
        this.hashService = hashService;
    }

    public boolean validatePassword(Userdata userdata, String enteredPassword) {
        byte[] salt = encodeString(userdata.getSalt());
        int hashLength = salt.length * 16;

        String hashedInput = hashService.hashString(enteredPassword, salt, userdata.getIterations(), hashLength);
        boolean valid = hashedInput.equals(userdata.getPassword());
        LOGGER.info("Userdata " + userdata.getUsername() + " password is " + valid);
        return valid;
    }

    private byte[] encodeString(String string) {
        return Base64.getDecoder().decode(string);
    }
}
