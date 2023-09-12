package com.teamip.heyhello.domain.report.entity;

import com.teamip.heyhello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Report {
    @Id @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestuser_id")
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "targetuser_id")
    private User targetUser;

    @Enumerated(value = EnumType.STRING)
    private ReportCategory reportCategory;

    @Builder
    public Report(User requestUser, User targetUser, String category) {
        this.requestUser = requestUser;
        this.targetUser = targetUser;
        this.reportCategory = ReportCategory.valueOf(category);
    }
}
