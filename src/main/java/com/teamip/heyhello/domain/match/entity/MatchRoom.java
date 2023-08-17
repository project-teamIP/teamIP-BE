package com.teamip.heyhello.domain.match.entity;

import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.global.audit.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MatchRoom{

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", referencedColumnName = "user_id")
    private User user1;

    @Column
    private UUID user1Client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", referencedColumnName = "user_id")
    private User user2;

    @Column
    private UUID user2Client;

    @Column
    private boolean isActive;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private  LocalDateTime closedAt;
    @Builder
    public MatchRoom(UUID roomName, User user1, UUID user1Client, User user2, UUID user2Client) {
        this.roomName = roomName;
        this.user1 = user1;
        this.user1Client = user1Client;
        this.user2 = user2;
        this.user2Client = user2Client;
        this.isActive = true;
    }

    public void updateIsActiveToFalse() {
        this.isActive = false;
    }
}
