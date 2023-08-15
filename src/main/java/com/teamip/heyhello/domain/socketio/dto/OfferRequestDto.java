package com.teamip.heyhello.domain.socketio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OfferRequestDto {

    private String userId;
    private String nickname;
    private String country;
    private String roomId;
    private String type;
    private String sdp;


    @Builder
    public OfferRequestDto(String userId, String nickname, String country, String roomId, String type, String sdp) {
        this.userId = userId;
        this.nickname = nickname;
        this.country = country;
        this.roomId = roomId;
        this.type = type;
        this.sdp = sdp;
    }
}
