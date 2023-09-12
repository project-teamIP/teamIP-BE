package com.teamip.heyhello.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StatusResponseDto {

    private final HttpStatus status;

    private final String message;

    @Builder
    public StatusResponseDto(HttpStatus status, String message){

        this.status = status;
        this.message = message;
    }
}
