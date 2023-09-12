package com.teamip.heyhello.domain.block.entity;

import com.teamip.heyhello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Block {
    @Id
    @Column(name = "block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestuser_id")
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "targetuser_id")
    private User targetUser;

    @Builder
    public Block(User requestUser, User targetUser) {
        this.requestUser = requestUser;
        this.targetUser = targetUser;
    }

}
