package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerResponseDto {

    private String userId;
    private OfferAndAnswerDto answer;

    @Builder
    public AnswerResponseDto(String userId, OfferAndAnswerDto answer) {
        this.userId = userId;
        this.answer = answer;
    }
}
