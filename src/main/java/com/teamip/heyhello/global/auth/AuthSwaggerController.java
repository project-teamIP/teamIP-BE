package com.teamip.heyhello.global.auth;

import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthSwaggerController extends SwaggerController {

    @Operation(summary = "액세스 토큰 재발급 요청", description = "액세스 토큰 만료 시 재발급 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> getAccessTokenByRefreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @RequestHeader("RefreshToken") String rtk,
                                                                          HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "로그아웃 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> logoutWithToken(@RequestHeader("AccessToken") String atk,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "토큰정보 출력", description = "토큰 정보 출력 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public String printRtkList(@AuthenticationPrincipal UserDetailsImpl userDetails);
}
