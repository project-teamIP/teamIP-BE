package com.teamip.heyhello.domain.socketio.repository;

import com.teamip.heyhello.domain.socketio.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
}
