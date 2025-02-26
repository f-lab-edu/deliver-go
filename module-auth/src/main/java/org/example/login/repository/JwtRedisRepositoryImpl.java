package org.example.login.repository;

import static org.example.login.constants.JwtProperties.KEY_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JwtRedisRepositoryImpl implements JwtRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void insert(String key, String jwt, long expire) {
        redisTemplate.opsForValue().set(KEY_PREFIX + key, jwt, expire);
    }

}
