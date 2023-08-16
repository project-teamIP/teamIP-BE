package com.teamip.heyhello.domain.match.entity;

import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.global.audit.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class MatchRoom extends TimeStamped {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", referencedColumnName = "user_id")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", referencedColumnName = "user_id")
    private User user2;

    @Column
    private boolean isActive;

    @Builder
    public MatchRoom(UUID roomName, User user1, User user2) {
        this.roomName = roomName;
        this.user1 = user1;
        this.user2 = user2;
        this.isActive = true;
    }

    public void updateIsActiveToFalse() {
        this.isActive = false;
    }
}
