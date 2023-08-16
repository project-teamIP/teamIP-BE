package com.teamip.heyhello.domain.match.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
