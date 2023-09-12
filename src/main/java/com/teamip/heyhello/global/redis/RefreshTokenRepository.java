package com.teamip.heyhello.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepository {
    private static final String RE_TKN = "refreshToken";
    private static final String LAST_EXCHANGED = "lastExchanged";
    private static final String RTK_EXP_STR = "rtk_expTime";
    private static final Integer MINUS_HOUR = 3;
    private static final Long EXP = 1000L * 60 * 60 * 24 * 14;
    private final RedisTemplate<String, String> redisTemplate;

    public String createAndSave(String loginId) {
        String rtk = UUID.randomUUID().toString();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(loginId, RE_TKN, rtk);
        hashOperations.put(loginId, RTK_EXP_STR, EXP + "");
        hashOperations.put(loginId, LAST_EXCHANGED, LocalDateTime.now().toString());
        redisTemplate.expire(loginId, EXP, TimeUnit.SECONDS);

        return rtk;
    }
    public String renewAndSave(String loginId, String refreshToken){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(loginId, RE_TKN, refreshToken);
        hashOperations.put(loginId, RTK_EXP_STR, EXP + "");
        hashOperations.put(loginId, LAST_EXCHANGED, LocalDateTime.now().toString());
        redisTemplate.expire(loginId, EXP, TimeUnit.SECONDS);

        return refreshToken;
    }
    public String findRefreshTokenByLoginId(String loginId){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return (String) hashOperations.get(loginId, RE_TKN);
    }
    public Optional<RefreshToken> findByLoginIdAndRefreshToken(String loginId, String rtk) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String findRtk = (String) hashOperations.get(loginId, RE_TKN);
        String expStr = (String) hashOperations.get(loginId, RTK_EXP_STR);

        if (!rtk.equals(findRtk)) {
            return Optional.empty();
        }
        hashOperations.put(loginId, LAST_EXCHANGED, LocalDateTime.now().toString());
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

    public int countRefreshTokens() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        LocalDateTime threeHoursBefore = LocalDateTime.now().minusHours(MINUS_HOUR);
        Set<String> keys = redisTemplate.keys("*");
        int count = 0;
        for (String key : keys) {
            DataType keyType = redisTemplate.type(key);
            if (keyType == DataType.HASH) {
                Object refreshToken = hashOperations.get(key, RE_TKN);
                LocalDateTime lastExchangedTime =LocalDateTime.parse((String)hashOperations.get(key, LAST_EXCHANGED));
                if (refreshToken != null && lastExchangedTime.isAfter(threeHoursBefore)) {
                    count++;
                }
            }
        }

        return count;
    }
}
