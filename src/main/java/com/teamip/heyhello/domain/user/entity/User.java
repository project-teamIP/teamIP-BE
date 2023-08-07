package com.teamip.heyhello.domain.user.entity;

import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean isKakao;

    @Column(nullable = false)
    private Boolean isGoogle;

    @Column(nullable = true)
    private String language;

    @Column(nullable = false)
    private Long cleanPoint;

    @Column(nullable = false)
    private Boolean isBlocked;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String interest;

    @Builder
    private User(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.cleanPoint = 50L;
        this.isKakao = Boolean.FALSE;
        this.isGoogle = Boolean.FALSE;
        this.isBlocked = Boolean.FALSE;
        this.country = "Default";
        this.gender = "Default";
        this.interest = "Default";
    }

    public static User of(SignupRequestDto signupRequestDto, String encodedPassword) {

        return User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .build();
    }

    public void initializeUserInfo(String country, String gender, String language, String interest) {
        this.country = country;
        this.gender = gender;
        this.language = language;
        this.interest = interest;
    }

    public void disableUserAccount() {
        this.isBlocked = Boolean.TRUE;
        System.out.println("유저가 밴당했써요...");
    }
}
