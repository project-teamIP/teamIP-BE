package com.teamip.heyhello.domain.match.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MatchUserInfoDto {

    private String nickname;
    private String country;
    private List<String> interests = new ArrayList<>();

    @Builder
    public MatchUserInfoDto(String nickname, String country, List<String>interests) {
        this.nickname = nickname;
        this.country = country;
        this.interests = interests;
    }
}
