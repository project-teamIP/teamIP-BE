package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUserDto {
    private Long userId;
    @Builder
    public RequestUserDto(Long userId) {
        this.userId = userId;
    }
}
