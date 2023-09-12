package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateProfileDto {
    private String nickname;
    private String language;
    private String gender;
    private String country;
    private List<String> interests;
}
