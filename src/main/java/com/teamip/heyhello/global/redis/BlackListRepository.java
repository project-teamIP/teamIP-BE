package com.teamip.heyhello.global.redis;

import com.teamip.heyhello.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
        String atk = blackListJwt.getAtk();
        long remainingTime = jwtUtil.getRemainingSeconds(blackListJwt.getAtk());

        ValueOperations<String ,String> valueOperations = blackList.opsForValue();
        valueOperations.set(atk, remainingTime + "");
        blackList.expire(atk, remainingTime, TimeUnit.SECONDS);
    }

    public Optional<BlackListJwt> getBlackList(String atk) {
        String loginId = jwtUtil.getLoginIdFromToken(atk);

        ValueOperations<String, String> valueOperations = blackList.opsForValue();
        String findAtk = (String) valueOperations.get(JwtUtil.BEARER_PREFIX + atk);
        return Optional.ofNullable(findAtk)
                .map(token -> new BlackListJwt(loginId, findAtk));
    }
}
