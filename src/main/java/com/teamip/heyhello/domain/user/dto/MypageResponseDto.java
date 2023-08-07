package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MypageResponseDto {
    private String nickname;
    private String email;
    private String country;
    private String interest;

    @Builder
    private MypageResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getLoginId().matches("\\d+") ? "Social login doesn't require an email" : user.getLoginId();
        this.country = user.getCountry();
        this.interest = user.getInterest();
    }

    public static MypageResponseDto of(User user) {

        return new MypageResponseDto(user);
    }

}
