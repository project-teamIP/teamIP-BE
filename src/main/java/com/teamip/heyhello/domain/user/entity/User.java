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


    @Builder
    private User(String loginId, String password, String nickname, String language) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.language = language;
        this.cleanPoint = 50L;
        this.isKakao = Boolean.FALSE;
        this.isGoogle = Boolean.FALSE;
        this.isBlocked = Boolean.FALSE;
    }


    public static User of(SignupRequestDto signupRequestDto, String encodedPassword) {

        return User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .language(signupRequestDto.getLanguage())
                .build();
    }

    public void disableUserAccount() {
        this.isBlocked = Boolean.TRUE;
        System.out.println("유저가 밴당했써요...");
    }
}
