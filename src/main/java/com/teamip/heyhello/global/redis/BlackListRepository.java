package com.teamip.heyhello.global.redis;

import com.teamip.heyhello.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class BlackListRepository {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> blackList;

    public BlackListRepository(JwtUtil jwtUtil, @Qualifier("redisTemplate") RedisTemplate<String, String> blackList) {
        this.jwtUtil = jwtUtil;
        this.blackList = blackList;
    }

    public void setBlackList(BlackListJwt blackListJwt) {
        String loginId = blackListJwt.getLoginId();
        String atk = blackListJwt.getAtk();
        long remainingTime = jwtUtil.getRemainingSeconds(blackListJwt.getAtk());

        HashOperations<String, String, Object> hashOperations = blackList.opsForHash();
        hashOperations.put(loginId, "accessToken", atk);
        hashOperations.put(loginId, "atk_expTime", remainingTime + "");
        blackList.expire(loginId, remainingTime, TimeUnit.SECONDS);
    }

    public Optional<BlackListJwt> getBlackList(String atk) {
        String loginId = jwtUtil.getLoginIdFromToken(atk);
        HashOperations<String, String, Object> hashOperations = blackList.opsForHash();
        String findAtk = (String) hashOperations.get(loginId, "accessToken");

        return Optional.ofNullable(findAtk)
                .map(token -> new BlackListJwt(loginId, findAtk));
    }
}
