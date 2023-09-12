package com.teamip.heyhello.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String loginId;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String country;
    @NotBlank
    private String gender;
    @NotBlank
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
