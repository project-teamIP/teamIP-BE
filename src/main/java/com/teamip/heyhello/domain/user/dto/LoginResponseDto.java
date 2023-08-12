package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;

    private String nickname;

    private String Language;

    public LoginResponseDto(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.Language = user.getLanguage();
    }
}
