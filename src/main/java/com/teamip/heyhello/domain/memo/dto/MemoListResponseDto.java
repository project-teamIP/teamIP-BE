package com.teamip.heyhello.domain.memo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemoListResponseDto {

    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String date;

    @Builder
    public MemoListResponseDto(Long id, String nickname, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        if(createdAt!=null) {
            int month = createdAt.getMonthValue();
            int day = createdAt.getDayOfMonth();
            String formattedDate = String.format("%02d/%02d", month, day);
            this.date = formattedDate;
        } else{
            this.date = "01/01";
        }
    }
}
