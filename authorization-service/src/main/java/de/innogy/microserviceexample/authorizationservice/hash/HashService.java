package de.innogy.microserviceexample.authorizationservice.hash;

import de.innogy.microserviceexample.authorizationservice.user.Userdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * Service to hash a users password.
 */
@Service
public class HashService {

    static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int KEY_LENGTH = 512;

    private Logger LOGGER = LoggerFactory.getLogger(HashService.class);

    private static final int ITERATIONS = 20000;

    public Userdata hashUserPassword(Userdata userdata) {

        byte[] salt = null;
        try {
            salt = generateSalt();
        } catch (NoSuchAlgorithmException nae) {
            LOGGER.error("Could not generate salt: ", nae);
        }
        if (salt != null) {
            String hashedPassword = hashString(userdata.getPassword(), salt, ITERATIONS, KEY_LENGTH);
            if (hashedPassword != null) {
                userdata.setPassword(hashedPassword);
                userdata.setSalt(decodeKey(salt));
                userdata.setIterations(ITERATIONS);
                return userdata;
            }
        }
        return null;
    }

    String hashString(String stringToHash, byte[] salt, int iterations, int keyLength) {
        requireNonNull(stringToHash);
        requireNonNull(salt);

        char[] passwordBytes = stringToHash.toCharArray();
        try {
            PBEKeySpec spec = new PBEKeySpec(passwordBytes, salt, iterations, keyLength);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
            return decodeKey(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            if (e.getClass().equals(NoSuchAlgorithmException.class)) {
                LOGGER.error("Algorithm " + KEY_FACTORY_ALGORITHM + " was not found", e);
            } else {
                LOGGER.error("KeySpec is invalid", e);
            }
        }
        return null;
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return salt;
    }

    private String decodeKey(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }
}
