package com.teamip.heyhello.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(RefreshToken rtk) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(rtk.getRefreshToken(), rtk.getLoginId());
        redisTemplate.expire(rtk.getRefreshToken(), 60L, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findByRefreshToken(String rtk) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String loginId = valueOperations.get(rtk);

        return Optional.ofNullable(loginId)
                .map(id -> new RefreshToken(rtk, loginId));
    }
}
