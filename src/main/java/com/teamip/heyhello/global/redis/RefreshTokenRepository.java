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
    private static final String RE_TKN = "refreshToken";
    private static final String RTK_EXP_STR = "rtk_expTime";
    private static final Long EXP = 1000L * 60 * 60 * 24 * 14;
    private final RedisTemplate<String, String> redisTemplate;

    public String createAndSave(String loginId) {
        String rtk = UUID.randomUUID().toString();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(loginId, RE_TKN, rtk);
        hashOperations.put(loginId, RTK_EXP_STR, EXP + "");
        redisTemplate.expire(loginId, EXP, TimeUnit.SECONDS);

        return rtk;
    }

    public Optional<RefreshToken> findByLoginIdAndRefreshToken(String loginId, String rtk) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String findRtk = (String) hashOperations.get(loginId, RE_TKN);
        String expStr = (String) hashOperations.get(loginId, RTK_EXP_STR);

        if (!rtk.equals(findRtk)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(loginId, findRtk, expStr));
    }

    public void deleteByRefreshToken(String loginId) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(loginId, RTK_EXP_STR);
        Long deletedId = hashOperations.delete(loginId, RE_TKN);
        if (deletedId == 0) {
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
