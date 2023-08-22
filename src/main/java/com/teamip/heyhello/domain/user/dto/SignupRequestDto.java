package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String loginId;
    private String password;
    private String nickname;
    private String country;
    private String gender;
    private String language;
    private List<String> interests;

    public SignupRequestDto(String loginId, String password, String nickname, String country, String gender, String language, List<String>interests) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.country = country;
        this.gender = gender;
        this.language = language;
        this.interests = interests;
    }
}
