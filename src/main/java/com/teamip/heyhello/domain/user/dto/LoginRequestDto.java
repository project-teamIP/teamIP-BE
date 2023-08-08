package com.teamip.heyhello.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    // 유효성 검사
    private String loginId;
    private String password;

    public LoginRequestDto(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
    }
}
