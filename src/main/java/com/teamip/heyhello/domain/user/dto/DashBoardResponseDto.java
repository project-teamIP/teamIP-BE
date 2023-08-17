package com.teamip.heyhello.domain.user.dto;

import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.memo.dto.MemoListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DashBoardResponseDto {
    private List<MatchRoomResponseDto> matchRoomInfos;
    private List<MemoListResponseDto> memos;
    private int userCount;

    @Builder
    public DashBoardResponseDto(List<MatchRoomResponseDto> matchRoomList, List<MemoListResponseDto> memoList, int userCount) {
        this.matchRoomInfos = matchRoomList;
        this.memos = memoList;
        this.userCount = userCount;
    }
}
