package com.teamip.heyhello.domain.socketio.repository;

import com.teamip.heyhello.domain.socketio.entity.MatchRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<MatchRoom, Long> {
}
