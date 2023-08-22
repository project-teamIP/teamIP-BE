package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;

    private String nickname;

    private String Language;
    private String image;
    private Long cleanPoint;
    @Builder
    public LoginResponseDto(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.Language = user.getLanguage();
        this.image = user.getImage();
        this.cleanPoint = user.getCleanPoint();
    }
}
