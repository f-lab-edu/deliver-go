package org.deliverygo.login.repository;

public interface JwtRepository {

    void insert(String key, String jwt, long expire);
}
