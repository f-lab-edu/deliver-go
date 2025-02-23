package org.example.login.repository;

import org.example.login.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    void register(User user);
}
