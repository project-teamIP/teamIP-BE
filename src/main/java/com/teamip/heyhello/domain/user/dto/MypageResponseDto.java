package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MypageResponseDto {
    private String nickname;
    private String loginId;
    private String country;
    private String interest;
    private String language;
    private String image;

    @Builder
    private MypageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.loginId = user.getLoginId().matches("\\d+") ? "Social login doesn't require an email" : user.getLoginId();
        this.country = user.getCountry();
        this.interest = user.getInterest();
        this.language = user.getLanguage();
        this.image = user.getImage();
    }

    public static MypageResponseDto of(User user) {

        return new MypageResponseDto(user);
    }

}
