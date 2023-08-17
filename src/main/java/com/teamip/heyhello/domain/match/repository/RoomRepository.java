package com.teamip.heyhello.domain.match.repository;

import com.teamip.heyhello.domain.match.entity.MatchRoom;
import com.teamip.heyhello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<MatchRoom, Long> {
    Optional<MatchRoom> findByRoomName(UUID sessionId);

    List<MatchRoom> findTop4ByUser1OrUser2OrderByCreatedAtDesc(User user, User user1);
}
