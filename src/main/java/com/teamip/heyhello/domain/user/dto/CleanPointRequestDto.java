package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CleanPointRequestDto {

    private String partnerNickname;

    private Long point;

    public CleanPointRequestDto(String partnerNickname, Long point) {
        this.partnerNickname = partnerNickname;
        this.point = point;
    }
}
