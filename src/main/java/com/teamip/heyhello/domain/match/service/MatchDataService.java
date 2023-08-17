package com.teamip.heyhello.domain.match.service;

import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.match.entity.MatchRoom;
import com.teamip.heyhello.domain.match.repository.RoomRepository;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchDataService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<MatchRoomResponseDto> getMatchRoomResponseDtos(User user) {
        List<MatchRoom> matchRooms = roomRepository.findTop4ByUser1OrUser2OrderByCreatedAtDesc(user, user);
        return matchRooms.stream().map(matchRoom -> {
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


    public Page<MatchRoomResponseDto> getUsersRecentCall(UserDetailsImpl userDetails, Pageable pageable) {
        Pageable editNumberPageable = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(() -> new NullPointerException("없는 유저입니다."));
        Page<MatchRoom> matchRooms = roomRepository.findByUser1OrUser2(user, user, editNumberPageable);
        return matchRooms.map(matchRoom -> {
                    User matchUser = (matchRoom.getUser1().equals(user)) ? matchRoom.getUser2() : matchRoom.getUser1();
                    return MatchRoomResponseDto.builder()
                            .image(matchUser.getImage())
                            .nickname(matchUser.getNickname())
                            .country(matchUser.getCountry())
                            .createdAt(matchRoom.getCreatedAt())
                            .closedAt(matchRoom.getClosedAt())
                            .build();
                }
        );
    }
}
