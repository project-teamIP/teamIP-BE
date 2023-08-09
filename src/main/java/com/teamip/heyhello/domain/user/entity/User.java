package com.teamip.heyhello.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamip.heyhello.domain.memo.entity.Memo;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.UpdateUserInfoDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
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
    private Boolean isLocked;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Memo> MemoList = new ArrayList<>();

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String interest;

    @Column(nullable = false)
    private String image;


    @Builder
    private User(String loginId, String password, String nickname, String image) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.cleanPoint = 50L;
        this.isKakao = Boolean.FALSE;
        this.isGoogle = Boolean.FALSE;
        this.isLocked = Boolean.FALSE;
        this.country = "Default";
        this.gender = "Default";
        this.interest = "Default";
        this.image = image;
    }

    public static User of(SignupRequestDto signupRequestDto, String encodedPassword, String defaultImageUrl) {

        return User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .image(defaultImageUrl)
                .build();
    }

    public void initializeUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        this.country = updateUserInfoDto.getCountry();
        this.gender = updateUserInfoDto.getGender();
        this.language = updateUserInfoDto.getLanguage();
        this.interest = updateUserInfoDto.getInterest();
    }

    public void disableUserAccount() {
        this.isLocked = Boolean.TRUE;
    }

    public void modifyProfileImage(String imageUrl) {
        this.image = imageUrl;
    }
}
