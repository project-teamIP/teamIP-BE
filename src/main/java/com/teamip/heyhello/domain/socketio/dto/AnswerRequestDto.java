package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {

    private String userId;
    private String roomId;
    private String type;
    private String sdp;

    @Builder
    public AnswerRequestDto(String userId, String roomId, String type, String sdp) {
        this.userId = userId;
        this.roomId = roomId;
        this.type = type;
        this.sdp = sdp;
    }
}
