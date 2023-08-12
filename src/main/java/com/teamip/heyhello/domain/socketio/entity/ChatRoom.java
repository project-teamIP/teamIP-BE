package com.teamip.heyhello.domain.socketio.entity;

import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.global.audit.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends TimeStamped {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String roomName;

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    @Column
    private boolean isActive;
}
