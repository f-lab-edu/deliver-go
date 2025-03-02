package org.deliverygo.login.repository;

import static org.deliverygo.login.constants.JwtProperties.KEY_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JwtRedisRepository implements JwtRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void insert(String key, String jwt, long expire) {
        redisTemplate.opsForValue().set(KEY_PREFIX + key, jwt, expire);
    }
}
