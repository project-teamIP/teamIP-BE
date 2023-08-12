package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OfferDto {

    private String userId;
    private String nickname;
    private String country;
    private String roomId;
    private String matchInfo;
    private String connection;

    @Builder
    public OfferDto(String userId, String nickname, String country, String roomId, String matchInfo, String connection) {
        this.userId = userId;
        this.nickname = nickname;
        this.country = country;
        this.roomId = roomId;
        this.matchInfo = matchInfo;
        this.connection = connection;
    }
}
