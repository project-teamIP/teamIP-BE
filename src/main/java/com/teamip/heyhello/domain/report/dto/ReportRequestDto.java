package com.teamip.heyhello.domain.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequestDto {
    private String nickname;
    private String category;

    public ReportRequestDto(String nickname, String category){
        this.nickname = nickname;
        this.category = category;
    }
}
