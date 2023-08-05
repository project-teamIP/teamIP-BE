package com.teamip.heyhello.domain.report.service;

import com.teamip.heyhello.domain.report.dto.ReportRequestDto;
import com.teamip.heyhello.domain.report.entity.Report;
import com.teamip.heyhello.domain.report.repository.ReportRepository;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.helper.UserFinder;
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

    private final UserFinder userFinder;
    private final ReportRepository reportRepository;

    @Transactional
    public StatusResponseDto reportUser(UserDetailsImpl userDetails, ReportRequestDto reportRequestDto) {
        User requestUser = userFinder.findUserByLoginId(userDetails.getUsername());
        User targetUser = userFinder.findUserByNickname(reportRequestDto.getNickname());

        checkRequestValidity(requestUser, targetUser);
        updateReportedCount(targetUser);

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
    // 24시간에 한번 씩 검사 & 신고 count+1 될 때마다 검사
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAllReportedCountPerDay() {
        List<User> userList = userFinder.findAll();
        userList.forEach(this::checkValueAboveMaxReportedCount);
    }

    private void updateReportedCount(User user) {
        if (user.getReportedCount() < MAX_REPORTED_COUNT) {
            user.updateReportedCount();
            checkValueAboveMaxReportedCount(user);
        }
    }

    private void checkValueAboveMaxReportedCount(User user) {
        if (user.getReportedCount() >= MAX_REPORTED_COUNT) {
            user.disableUserAccount();
        }
    }

    private void checkRequestValidity(User requestUser, User targetUser) {
        if (requestUser.getNickname().equals(targetUser.getNickname())) {
            throw new RuntimeException("자기 자신을 신고할 수 없습니다.");
        }

        Optional<User> findUser = reportRepository.findByRequestUserAndTargetUser(requestUser, targetUser);
        if (findUser.isPresent()) {
            throw new RuntimeException("같은 유저는 신고할 수 없습니다.");
        }
    }

}
