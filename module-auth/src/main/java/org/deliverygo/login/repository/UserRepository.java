package org.deliverygo.login.repository;

import org.deliverygo.login.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    void register(User user);
}
