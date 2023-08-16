package com.teamip.heyhello.domain.socketio.entity;

import com.teamip.heyhello.domain.socketio.dto.WaitUserDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@Getter
public class MatchUserList {

    private final Map<UUID, WaitUserDto> lists = new LinkedHashMap<>();

    private final RedisTemplate<String, String> matchUserList;


    public MatchUserList(@Qualifier("redisTemplate") RedisTemplate<String, String> matchUserList) {
        this.matchUserList = matchUserList;
    }
}