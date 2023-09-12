package com.teamip.heyhello.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UrlResponseDto {
    private String url;

    @Builder
    public UrlResponseDto(String url) {
        this.url = url;
    }
}
