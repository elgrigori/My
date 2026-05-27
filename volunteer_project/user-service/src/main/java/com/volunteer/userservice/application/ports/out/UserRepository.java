package com.volunteer.userservice.application.ports.out;

import com.volunteer.userservice.application.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface UserRepository extends PanacheRepository<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
