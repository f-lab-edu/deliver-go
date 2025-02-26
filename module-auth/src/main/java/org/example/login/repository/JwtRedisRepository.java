package org.example.login.repository;

public interface JwtRedisRepository {

    void insert(String key, String jwt, long expire);

}
