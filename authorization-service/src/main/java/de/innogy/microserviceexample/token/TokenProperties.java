package de.innogy.microserviceexample.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class TokenProperties {

    @Getter
    @Setter
    private long maxAgeSeconds;
    @Getter
    @Setter
    private String secret;

    public TokenProperties(long maxAgeSeconds, String secret) {
        this.maxAgeSeconds = maxAgeSeconds;
        this.secret = secret;
    }


}
