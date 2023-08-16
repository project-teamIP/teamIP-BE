package com.teamip.heyhello.domain.socketio.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@Getter
public class MatchRoomList {

    private final Map<UUID, MatchRoom> lists = new LinkedHashMap<>();

}