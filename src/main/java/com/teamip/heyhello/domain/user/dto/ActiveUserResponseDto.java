package com.teamip.heyhello.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActiveUserResponseDto {

    private int activeUser;

    public ActiveUserResponseDto(int activeUser){
        this.activeUser = activeUser;
    }
}
