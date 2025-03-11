package org.deliverygo.login.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static org.deliverygo.login.domain.JwtToken.KEY_PREFIX;

@Repository
@RequiredArgsConstructor
public class JwtRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void insertJwt(String key, String jwt, long expire) {
        redisTemplate.opsForValue().set(KEY_PREFIX + ":" + key, jwt, expire);
    }
}
