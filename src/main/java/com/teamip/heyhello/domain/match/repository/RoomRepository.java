package com.teamip.heyhello.domain.match.repository;

import com.teamip.heyhello.domain.match.entity.MatchRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<MatchRoom, Long> {
    Optional<MatchRoom> findByRoomName(UUID sessionId);

}
