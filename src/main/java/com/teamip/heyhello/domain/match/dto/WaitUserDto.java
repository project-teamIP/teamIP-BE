package com.teamip.heyhello.domain.match.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@JsonSerialize
@JsonDeserialize
public class WaitUserDto {
    private Long userId;
    private String nickname;
    private String language;

    private UUID sessionId;
    @Builder
    public WaitUserDto(User user, UUID sessionId) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.language = user.getLanguage();
        this.sessionId = sessionId;
    }
}
