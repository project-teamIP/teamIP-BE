package com.teamip.heyhello.domain.report.repository;

import com.teamip.heyhello.domain.report.entity.Report;
import com.teamip.heyhello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<User> findByRequestUserAndTargetUser(User requestUser, User targetUser);

    Optional<User> findByTargetUser(User user);
}
