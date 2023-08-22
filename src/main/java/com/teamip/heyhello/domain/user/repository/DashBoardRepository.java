package com.teamip.heyhello.domain.user.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamip.heyhello.domain.hourtraffic.entity.HourTraffic;
import com.teamip.heyhello.domain.hourtraffic.repository.HourTrafficRepository;
import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.match.entity.QMatchRoom;
import com.teamip.heyhello.domain.memo.dto.MemoListResponseDto;
import com.teamip.heyhello.domain.memo.entity.QMemo;
import com.teamip.heyhello.domain.user.dto.DashBoardResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashBoardRepository {
    private final RefreshTokenRepository refreshTokenRepository;
    private final HourTrafficRepository hourTrafficRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;

    public DashBoardResponseDto getDashBoard(UserDetailsImpl userDetails) {
        User user = getUser(userDetails);

        List<MatchRoomResponseDto> matchRoomResponseDtos = getMatchResponseDtos(user);
        List<MemoListResponseDto> memoListResponseDtos = getMemoListReponseDtos(user);
        int cntUser = countActiveUser();
        List<Integer> nowUserList = todayUserCountListByHour();

        return DashBoardResponseDto
                .builder()
                .matchRoomList(matchRoomResponseDtos)
                .memoList(memoListResponseDtos)
                .userCount(cntUser)
                .userCountList(nowUserList)
                .build();
    }

    private List<Integer> todayUserCountListByHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);

        List<HourTraffic> hourlyTrafficList = hourTrafficRepository.findByTimeBetween(startOfDay, endOfDay);
        return hourlyTrafficList.stream().map(HourTraffic::getUserCount).toList();
    }

    private User getUser(UserDetailsImpl userDetails) {
        String loginId = userDetails.getUsername();
        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
    }

    private List<MemoListResponseDto> getMemoListReponseDtos(User user) {
        QMemo m = QMemo.memo;
        List<Tuple> result =
                jpaQueryFactory
                        .select(m.id, m.content, m.title, m.user.nickname, m.createdAt)
                        .from(m)
                        .where(m.user.id.eq(user.getId()))
                        .orderBy(m.createdAt.desc())
                        .limit(6)
                        .fetch();

        return convertMemosToDtos(result);
    }

    private List<MatchRoomResponseDto> getMatchResponseDtos(User user) {
        QMatchRoom mr = QMatchRoom.matchRoom;

        List<Tuple> result =
                jpaQueryFactory
                        .select(mr.createdAt, mr.user1.id, mr.user2.id, mr.user1.nickname, mr.user2.nickname, mr.user1.country, mr.user2.country, mr.user1.image, mr.user2.image)
                        .from(mr)
                        .where(mr.user1.id.eq(user.getId()).or(mr.user2.id.eq(user.getId())))
                        .orderBy(mr.createdAt.desc())
                        .limit(4)
                        .fetch();

        return convertMatchRoomsToDtos(result, user);
    }

    private List<MatchRoomResponseDto> convertMatchRoomsToDtos(List<Tuple> tuples, User user) {
        QMatchRoom mr = QMatchRoom.matchRoom;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return tuples.stream()
                .map(tuple -> {
                            Long user1Id = tuple.get(mr.user1.id);

                            String nickname = user1Id.equals(user.getId()) ? tuple.get(mr.user2.nickname) : tuple.get(mr.user1.nickname);
                            String image = user1Id.equals(user.getId()) ? tuple.get(mr.user2.image) : tuple.get(mr.user1.image);
                            String country = user1Id.equals(user.getId()) ? tuple.get(mr.user2.country) : tuple.get(mr.user1.country);

                            String date = tuple.get(mr.createdAt).format(dateFormatter);
                            String time = tuple.get(mr.createdAt).format(timeFormatter);

                            return new MatchRoomResponseDto(image, nickname, country, date, time);
                        }
                )
                .toList();
    }

    private List<MemoListResponseDto> convertMemosToDtos(List<Tuple> tuples) {
        QMemo m = QMemo.memo;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");

        return tuples.stream()
                .map(tuple -> {
                            Long id = tuple.get(m.id);
                            String content = tuple.get(m.content);
                            String title = tuple.get(m.title);
                            String nickname = tuple.get(m.partnerNickname);
                            String date = tuple.get(m.createdAt).format(dateFormatter);

                            return new MemoListResponseDto(id, nickname, title, content, date);
                        }
                )
                .toList();
    }

    private int countActiveUser() {
        int activeUser = 0;
        activeUser = refreshTokenRepository.countRefreshTokens();
        return activeUser;
    }
}
