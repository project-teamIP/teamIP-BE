package com.teamip.heyhello.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuddyResponseDto {
    private String nickname;
    private String loginId;
    private String profileImage;

    @Builder
    public BuddyResponseDto(String nickname, String loginId, String image) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.profileImage = image;
    }
}
