package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IceDto {

    private String roomId;
    private String candidate;

    @Builder
    public IceDto(String roomId, String candidate) {
        this.roomId = roomId;
        this.candidate = candidate;
    }
}
