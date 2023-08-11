package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String id;

    public KakaoUserInfoDto(String id) {
        this.id = id;
    }
}