package org.example.login.repository;

import org.example.login.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public Optional<User> findByEmail(String email) {
//        User user = entityManager.find(User.class, email);
        return Optional.ofNullable(null);
    }

    @Override
    public void register(User user) {
        
    }
}
