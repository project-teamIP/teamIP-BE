package com.teamip.heyhello.global.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60L)
public class RefreshToken {
    @Id
    private String refreshToken;
    private String loginId;
}
