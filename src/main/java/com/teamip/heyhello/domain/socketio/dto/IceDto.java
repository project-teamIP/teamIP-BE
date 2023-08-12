package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class IceDto {

    private String userId;
    private String roomId;
    private String ice;
    private String connection;

    @Builder
    public IceDto(String userId, String roomId, String ice, String connection) {
        this.userId = userId;
        this.roomId = roomId;
        this.ice = ice;
        this.connection = connection;
    }
}
