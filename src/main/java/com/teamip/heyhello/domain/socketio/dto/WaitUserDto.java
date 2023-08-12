package com.teamip.heyhello.domain.socketio.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class WaitUserDto {
    private User user;

    private UUID sessionId;
    @Builder
    public WaitUserDto(User user, UUID sessionId) {
        this.user = user;
        this.sessionId = sessionId;
    }
}
