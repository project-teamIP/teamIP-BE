package com.teamip.heyhello.domain.match.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchRoomResponseDto {
    private String image;
    private String nickname;
    private String country;
    private String date;
    private String time;

    @Builder
    public MatchRoomResponseDto(String image, String nickname, String country, LocalDateTime createdAt, LocalDateTime closedAt) {
        this.image = image;
        this.nickname = nickname;
        this.country = country;
        if(createdAt!=null&&closedAt!=null) {
            int month = createdAt.getMonthValue();
            int day = createdAt.getDayOfMonth();
            String formattedDate = String.format("%02d/%02d", month, day);
            this.date = formattedDate;
            long callTime = Duration.between(createdAt, closedAt).getSeconds();

            long hours = callTime / 3600;
            long minutes = (callTime % 3600) / 60;
            long remainingSeconds = callTime % 60;
            this.time = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        }else{
            this.date = "01/01";
            this.time = "00:00:00";
        }
    }

    public MatchRoomResponseDto(String image, String nickname, String country, String date, String time) {
        this.image = image;
        this.nickname = nickname;
        this.country = country;
        this.date = date;
        this.time = time;

    }

}
