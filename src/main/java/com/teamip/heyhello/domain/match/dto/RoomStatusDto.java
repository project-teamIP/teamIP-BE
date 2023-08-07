package com.teamip.heyhello.domain.match.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomStatusDto {

    private User user1;
    private User user2;
    private String user1Endpoint;
    private String user2Endpoint;
    private LocalDateTime createdAt;

    @Builder
    public RoomStatusDto(User user1, User user2, String user1Endpoint, String user2Endpoint) {
        this.user1 = user1;
        this.user2 = user2;
        this.user1Endpoint = user1Endpoint;
        this.user2Endpoint = user2Endpoint;
        this.createdAt = LocalDateTime.now();
    }
}
