package de.innogy.microserviceexample.authorizationservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Userdata, Long> {

    @Query("SELECT u FROM Userdata u LEFT JOIN FETCH u.roles r WHERE u.username=?1")
    public Userdata getUserAndRolesByUsername(String username);

    public Userdata findByUsername(String username);
}
