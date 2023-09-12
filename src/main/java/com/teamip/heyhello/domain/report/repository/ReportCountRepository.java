package com.teamip.heyhello.domain.report.repository;

import com.teamip.heyhello.domain.report.entity.ReportCategory;
import com.teamip.heyhello.domain.report.entity.ReportCount;
import com.teamip.heyhello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportCountRepository extends JpaRepository<ReportCount, Long> {

    Optional<ReportCount> findByUserAndCategory(User user, ReportCategory category);

    List<ReportCount> findByUserId(Long userId);
}
