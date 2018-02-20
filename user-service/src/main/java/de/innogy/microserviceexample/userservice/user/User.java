package de.innogy.microserviceexample.userservice.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private long id;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FIRSTNAME")
    @Size(min = 2, max = 255)
    private String firstname;

    @Size(min = 2, max = 255)
    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "BIRTHDAY")
    @Past
    private Date birthday;

    @Column(name = "EMAIL")
    @Email
    @Size(min = 6, max = 255)
    private String email;

    public String getName(){
        return firstname + " " + lastname;
    }
}
