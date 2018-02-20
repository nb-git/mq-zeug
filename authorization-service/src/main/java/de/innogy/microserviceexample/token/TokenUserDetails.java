package de.innogy.microserviceexample.token;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class TokenUserDetails extends User {

    private String token;
    private String profileName;

    public TokenUserDetails(String username, String token, String profileName, String password, Collection<? extends GrantedAuthority> authorities){
        super(username, password, true, true, true, true, authorities);
        this.profileName = profileName;
        this.token = token;
    }


}
