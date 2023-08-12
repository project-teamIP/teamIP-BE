package com.teamip.heyhello.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private static final Long EXP = 1000L * 60 * 60 * 24 * 14;
    private final RedisTemplate<String, String> redisTemplate;

    public String createAndSave(String loginId) {
        String rtk = UUID.randomUUID().toString();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(loginId, "refreshToken", rtk);
        hashOperations.put(loginId, "rtk_expTime", EXP + "");
        redisTemplate.expire(loginId, EXP, TimeUnit.SECONDS);

        return rtk;
    }

    public Optional<RefreshToken> findByLoginId(String loginId) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String rtk = (String) hashOperations.get(loginId, "refreshToken");
        String expStr = (String) hashOperations.get(loginId, "rtk_expTime");

        return Optional.ofNullable(rtk)
                .map(id -> new RefreshToken(loginId, rtk, expStr));
    }

    public void deleteByRefreshToken(String loginId) {
        System.out.println("loginId = " + loginId);
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(loginId, "rtk_expTime");
        Long deletedId = hashOperations.delete(loginId, "refreshToken");
        if(deletedId == 0) {
            throw new RuntimeException("리프레시토큰이 존재하지 않습니다.");
        }
    }

    public String printHashOpsByLoginId(String loginId) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> entries = hashOperations.entries(loginId);
        StringBuilder sb = new StringBuilder();
        sb.append("▽▽▽▽▽▽▽▽▽▽▽\n");
        sb.append(loginId + ": {\n");
        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            sb.append("    Key: " + entry.getKey() + ", Value: " + entry.getValue() + "s\n");
        }
        sb.append("}\n");
        sb.append("△△△△△△△△△△△\n");

        return sb.toString();
    }
}
