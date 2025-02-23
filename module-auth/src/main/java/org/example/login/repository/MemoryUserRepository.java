package org.example.login.repository;

import org.example.login.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public class MemoryUserRepository implements UserRepository {

    private final Map<String, User> map = new HashMap<>();
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(map.get(email));
    }

    @Override
    public void register(User user) {
        user.setId(UUID.randomUUID().toString());
        map.put(user.getEmail(), user);
    }
}
