package com.teamip.heyhello.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column
    private LocalDateTime withdrawalTime;

    public UserLog(User userInfo, UserStatus status, LocalDateTime withdrawalTime) {
        this.user = userInfo;
        this.status = status;
        this.withdrawalTime = withdrawalTime;
    }
    public void setStatus(UserStatus userStatus) {
        this.status = userStatus;
    }

    public void setWithdrawalTime(LocalDateTime withdrawalTime){
        this.withdrawalTime = withdrawalTime;
    }


    public boolean isRejoinAllowed() {
        if (status != UserStatus.WITHDRAWAL) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rejoinTime = withdrawalTime.plusSeconds(20);

        return now.isAfter(rejoinTime);
    }


}
