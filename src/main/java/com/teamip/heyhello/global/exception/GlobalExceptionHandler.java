package com.teamip.heyhello.global.exception;

import com.teamip.heyhello.global.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponseDto> handleAll(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(StatusResponseDto.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage())
                        .build());
    }
}
