package com.teamip.heyhello.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamip.heyhello.domain.memo.entity.Memo;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.UpdateProfileDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    private User(String loginId, String password, String nickname, String image, String country, String gender, String language, String interest) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.cleanPoint = 50L;
        this.isKakao = Boolean.FALSE;
        this.isGoogle = Boolean.FALSE;
        this.isLocked = Boolean.FALSE;
        this.country = defaultValue(country);
        this.gender = defaultValue(gender);
        this.interest = defaultValue(interest);
        this.language = defaultValue(language);
        this.image = image;
        this.status = UserStatus.ACTIVE;
    }

    private String defaultValue(String value) {
        return value == null ? "Default" : value;
    }

    public static User of(SignupRequestDto signupRequestDto, String encodedPassword, String defaultImageUrl) {

        return User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .country(signupRequestDto.getCountry())
                .gender(signupRequestDto.getGender())
                .language(signupRequestDto.getLanguage())
                .interest(signupRequestDto.getInterest())
                .image(defaultImageUrl)
                .build();
    }

    public void disableUserAccount() {
        this.isLocked = Boolean.TRUE;
    }

    public void modifyProfileImage(String imageUrl) {
        this.image = imageUrl;
    }

    public void update(UpdateProfileDto updateProfileDto) {
        this.nickname = Optional.ofNullable(updateProfileDto.getNickname()).orElse(this.nickname);
        this.language = Optional.ofNullable(updateProfileDto.getLanguage()).orElse(this.language);
        this.gender = Optional.ofNullable(updateProfileDto.getGender()).orElse(this.gender);
        this.country = Optional.ofNullable(updateProfileDto.getCountry()).orElse(this.country);
        this.interest = Optional.ofNullable(updateProfileDto.getInterest()).orElse(interest);
    }

    public void setStatus(UserStatus userStatus) {
        this.status = status;
    }

}
