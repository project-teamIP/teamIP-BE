package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OfferResponseDto {

    private String userId;
    private String nickname;
    private String country;
    private String roomId;
    private OfferAndAnswerDto offer;


    @Builder
    public OfferResponseDto(String userId, String nickname, String country, String roomId, OfferAndAnswerDto offer) {
        this.userId = userId;
        this.nickname = nickname;
        this.country = country;
        this.roomId = roomId;
        this.offer = offer;
    }
}
