package de.innogy.microserviceexample.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import de.innogy.microserviceexample.authorizationservice.user.Role;
import de.innogy.microserviceexample.authorizationservice.user.Userdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private TokenProperties tokenProperties;
    private String issuer;
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;

    public TokenService(TokenProperties tokenProperties, String issuer, Algorithm algorithm) throws UnsupportedEncodingException {
        this.tokenProperties = tokenProperties;
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
        this.jwtVerifier = JWT.require(algorithm).acceptExpiresAt(0).build();
    }

    //FIXME
    public String encode(Userdata user) throws TokenCreationException {
        LocalDateTime now = LocalDateTime.now();
        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUsername())
                    .withIssuedAt(Date
                            .from(now.atZone(ZoneId.systemDefault())
                                    .toInstant()))
                    .withExpiresAt(Date
                            .from(now.plusSeconds(tokenProperties.getMaxAgeSeconds())
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()))
                    .withArrayClaim("role", user
                            .getRoles()
                            .stream()
                            .map(Role::getRoles)
                            .toArray(String[]::new))
                    .withClaim("usr", user.getUsername())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            LOGGER.error("Cannot properly create token", ex);
            throw new TokenCreationException("Cannot properly create token", ex);
        }
    }
}
