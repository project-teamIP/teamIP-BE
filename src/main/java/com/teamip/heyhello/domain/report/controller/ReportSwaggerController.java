package com.teamip.heyhello.domain.report.controller;

import com.teamip.heyhello.domain.report.dto.ReportRequestDto;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReportSwaggerController extends SwaggerController {


    @Operation(summary = "신고", description = "대상 유저를 신고하는 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> report(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody ReportRequestDto reportRequestDto);
}
