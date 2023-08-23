package com.teamip.heyhello.domain.user.controller;

import com.teamip.heyhello.domain.user.dto.CleanPointRequestDto;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface TestController extends SwaggerController {
    @Operation(summary = "회원 가입", description = "이메일을 통한 회원 가입 요청입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class))})
    })
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto);


    @Operation(summary = "통화 종료 후 점수 매기기", description = "통화가 종료된 후 상대방의 매너 점수를 매깁니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)) })
    })
    public ResponseEntity<StatusResponseDto> rateCleanPoint(@RequestHeader("AccessToken") String tokenValue,
                                                            @RequestBody CleanPointRequestDto requestDto);
}