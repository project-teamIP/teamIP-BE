package com.teamip.heyhello.domain.match.dto;

import com.teamip.heyhello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchInfoRequestDto {
    private String userLanguage;
    private String targetLanguage;
    private String userEndpoint;
    private User user;
    private boolean isMatched = false;
    private LocalDateTime createdAt;
    @Builder
    public MatchInfoRequestDto(String userLanguage, String targetLanguage, String userEndpoint, User user) {
        this.userLanguage = userLanguage;
        this.targetLanguage = targetLanguage;
        this.userEndpoint = userEndpoint;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void successMatch(MatchInfoRequestDto matchInfoRequestDto){
        this.isMatched = true;
    }
}
