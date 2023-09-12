package com.teamip.heyhello.domain.match.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class MatchListResponseDto {
    Page<MatchRoomResponseDto> matchRooms;

    @Builder
    public MatchListResponseDto(Page<MatchRoomResponseDto> matchRooms) {
        this.matchRooms = matchRooms;
    }
}
