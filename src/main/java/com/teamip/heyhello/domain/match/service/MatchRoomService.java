package com.teamip.heyhello.domain.match.service;

import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.match.entity.MatchRoom;
import com.teamip.heyhello.domain.match.repository.RoomRepository;
import com.teamip.heyhello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchRoomService {
    private final RoomRepository roomRepository;

    public List<MatchRoomResponseDto> getMatchRoomResponseDtos(User user) {
        List<MatchRoom> matchRooms = roomRepository.findTop4ByUser1OrUser2OrderByCreatedAtDesc(user, user);
        return matchRooms.stream().map(matchRoom-> {
                    User matchUser = (matchRoom.getUser1().equals(user)) ? matchRoom.getUser2() : matchRoom.getUser1();
                    return MatchRoomResponseDto.builder()
                            .image(matchUser.getImage())
                            .nickname(matchUser.getNickname())
                            .country(matchUser.getCountry())
                            .createdAt(matchRoom.getCreatedAt())
                            .closedAt(matchRoom.getClosedAt())
                            .build();
                }
        ).toList();
    }
}
