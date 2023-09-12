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
    private List<Integer> countByHour;

    // user정보로 가져오지만 user정보는 select하지 않는
    // select matchroom.image, nickname, createdAt, closedAt, country / memolist.id, nickname, title, content, createdAt
    // or 객체로 묶여서 오는지



    @Builder
    public DashBoardResponseDto(List<MatchRoomResponseDto> matchRoomList, List<MemoListResponseDto> memoList, int userCount, List<Integer> userCountList) {
        this.matchRoomInfos = matchRoomList;
        this.memos = memoList;
        this.userCount = userCount;
        this.countByHour = userCountList;
    }
}
