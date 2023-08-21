package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.user.entity.Interest;
import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MypageResponseDto {
    private String nickname;
    private String loginId;
    private String country;
    private List<String> interests;
    private String language;
    private String image;

    @Builder
    private MypageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.loginId = user.getLoginId().matches("\\d+") ? "Social login doesn't require an email" : user.getLoginId();
        this.country = user.getCountry();
        this.interests = user.getInterests().stream().map(Interest::getName).collect(Collectors.toList());
        this.language = user.getLanguage();
        this.image = user.getImage();
    }

    public static MypageResponseDto of(User user) {

        return new MypageResponseDto(user);
    }

}
