package de.innogy.microserviceexample.authorizationservice.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
public class Userdata {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    @NotNull
    @Size(min = 2, max = 255)
    private String username;

    @Column(name = "PASSWORD")
    @NotNull
    @Size(min = 5, max = 255)
    private String password;

    private Integer iterations;

    private String salt;

    @ManyToMany
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private List<Role> roles;



}
