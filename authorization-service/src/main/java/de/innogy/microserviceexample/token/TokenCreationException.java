package de.innogy.microserviceexample.token;

import com.auth0.jwt.exceptions.JWTCreationException;

public class TokenCreationException extends Throwable {
    public TokenCreationException(String cannot_properly_create_token, JWTCreationException ex) {
    }
}
