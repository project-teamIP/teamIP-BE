package com.teamip.heyhello.domain.report.controller;

import com.teamip.heyhello.domain.report.dto.ReportRequestDto;
import com.teamip.heyhello.domain.report.service.ReportService;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReportSwaggerController {
    private final ReportService reportService;

    @PostMapping("/api/users/report")
    public ResponseEntity<StatusResponseDto> report(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody ReportRequestDto reportRequestDto) {

        return ResponseEntity.ok(reportService.reportUser(userDetails, reportRequestDto));
    }
}
