package com.teamip.heyhello.domain.report.service;

import com.teamip.heyhello.domain.report.dto.ReportRequestDto;
import com.teamip.heyhello.domain.report.entity.Report;
import com.teamip.heyhello.domain.report.entity.ReportCategory;
import com.teamip.heyhello.domain.report.entity.ReportCount;
import com.teamip.heyhello.domain.report.repository.ReportCountRepository;
import com.teamip.heyhello.domain.report.repository.ReportRepository;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final int MAX_REPORTED_COUNT = 5;

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportCountRepository reportCountRepository;

    @Transactional
    public StatusResponseDto reportUser(UserDetailsImpl userDetails, ReportRequestDto reportRequestDto) {
        User requestUser = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        User targetUser = userRepository.findByNickname(reportRequestDto.getNickname()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        checkRequestValidity(requestUser, targetUser);
        updateReportedCount(targetUser, reportRequestDto.getCategory());

        Report report = Report.builder()
                .requestUser(requestUser)
                .targetUser(targetUser)
                .category(reportRequestDto.getCategory())
                .build();

        reportRepository.save(report);

        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message("신고가 완료되었습니다.")
                .build();
    }

    // 동일 사유로 5번 신고당하면 계정 비활성화
    // 매일 자정에 검사 & 신고 count+1 될 때마다 검사
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAllReportedCountPerDay() {
        List<User> userList = userRepository.findAll();
        userList.forEach(this::checkValueAboveMaxReportedCount);
    }

    private void updateReportedCount(User user, String category) {
        if (reportExisted(user, category)) {
            ReportCount reportCount = reportCountRepository.findByUserAndCategory(user, ReportCategory.valueOf(category)).get();
            reportCount.updateCount();
            reportCountRepository.save(reportCount);
            checkValueAboveMaxReportedCount(user);
            return;
        }
        ReportCount reportCount = new ReportCount(user, ReportCategory.valueOf(category));
        reportCountRepository.save(reportCount);
    }

    private boolean reportExisted(User user, String category) {
        Optional<ReportCount> reportCount = reportCountRepository.findByUserAndCategory(user, ReportCategory.valueOf(category));
        return reportCount.isPresent();
    }

    private void checkValueAboveMaxReportedCount(User user) {
        Long userId = user.getId();
        List<ReportCount> reportCountList = reportCountRepository.findByUserId(userId);
        reportCountList.forEach(
                reportCount -> {
                    System.out.println("reportCount.getCount() = " + reportCount.getCount());
                    if (reportCount.getCount() >= MAX_REPORTED_COUNT) {
                        user.disableUserAccount();
                    }
                }
        );
    }

    private void checkRequestValidity(User requestUser, User targetUser) {
        if (requestUser.getNickname().equals(targetUser.getNickname())) {
            throw new RuntimeException("자기 자신을 신고할 수 없습니다.");
        }

        Optional<Report> report = reportRepository.findByRequestUserAndTargetUser(requestUser, targetUser);
        if (report.isPresent()) {
            throw new RuntimeException("같은 유저는 신고할 수 없습니다.");
        }
    }

}
