package com.teamip.heyhello.domain.report.entity;

import com.teamip.heyhello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
public class ReportCount {
    @Id @Column(name = "reportcount_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private ReportCategory category;

    @Column(nullable = false)
    private int count;

    public ReportCount(User user, ReportCategory category) {
        this.user = user;
        this.category = category;
        this.count = 1;
    }

    public void updateCount() {
        this.count++;
    }
}
