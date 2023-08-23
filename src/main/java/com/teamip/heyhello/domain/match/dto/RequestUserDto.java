package com.teamip.heyhello.domain.match.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUserDto {
    private String loginId;
    @Builder
    public RequestUserDto(String loginId) {
        this.loginId = loginId;
    }
}
