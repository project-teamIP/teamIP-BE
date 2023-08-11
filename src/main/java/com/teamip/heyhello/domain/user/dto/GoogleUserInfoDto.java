package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfoDto {
    private String id;

    public GoogleUserInfoDto(String id) {
        this.id = id;
    }
}

