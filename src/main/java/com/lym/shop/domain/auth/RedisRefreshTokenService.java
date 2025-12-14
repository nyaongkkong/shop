package com.lym.shop.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "refresh:";

    public void save(String username, String refreshToken, Duration ttl) {
        String key = PREFIX + username;
        stringRedisTemplate.opsForValue().set(key, refreshToken, ttl);
    }

    public String get(String username) {
        String key = PREFIX + username;
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String username) {
        String key = PREFIX + username;
        stringRedisTemplate.delete(key);
    }
}