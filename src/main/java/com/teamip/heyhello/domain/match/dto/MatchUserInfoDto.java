package com.teamip.heyhello.domain.match.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class MatchUserInfoDto {

    private UUID roomId;
    private String nickname;
    private String country;

    @Builder
    public MatchUserInfoDto(UUID roomId, String nickname, String country) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.country = country;
    }
}
