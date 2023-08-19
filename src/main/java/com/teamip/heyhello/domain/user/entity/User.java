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

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Long cleanPoint;

    @Column(nullable = false)
    private Boolean isLocked;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Memo> MemoList = new ArrayList<>();

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests = new ArrayList<>();

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    private User(String loginId, String password, String nickname, String image, String country, String gender, String language) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.cleanPoint = 50L;
        this.isKakao = Boolean.FALSE;
        this.isGoogle = Boolean.FALSE;
        this.isLocked = Boolean.FALSE;
        this.country = defaultValue(country);
        this.gender = defaultValue(gender);
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
        if (updateProfileDto.getInterests() != null&& !updateProfileDto.getInterests().isEmpty()) {
            this.interests.clear();
            for (String interestName : updateProfileDto.getInterests()) {
                this.interests.add(new Interest(interestName, this));
            }
        }
    }

    public void setStatus(UserStatus userStatus) {
        this.status = status;
    }

    public void updateCleanPoint(Long point) {
        if (point < -10 || point > 10) {
            throw new IllegalArgumentException("매너 점수는 -10 ~ +10 사이로 입력해주세요.");
        } else {
            this.cleanPoint = cleanPoint + point;
        }
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
        for (Interest interest : interests) {
            interest.setUser(this);
        }
    }
}
