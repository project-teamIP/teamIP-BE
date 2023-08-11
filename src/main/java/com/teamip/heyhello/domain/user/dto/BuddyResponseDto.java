package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuddyResponseDto {
    private String nickname;
    private String loginId;

    public BuddyResponseDto(String nickname, String loginId) {
        this.nickname = nickname;
        this.loginId = loginId;
    }
}
