package com.teamip.heyhello.domain.match.controller;

import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.user.dto.MypageResponseDto;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

public interface MatchDataSwaggerController extends SwaggerController {

    @Operation(summary = "최근 통화목록 조회", description = "최근 통화목록 정보 요청 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MatchRoomResponseDto.class)),})
    })
    public ResponseEntity<Page<MatchRoomResponseDto>> getUsersRecentCall(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @PageableDefault(
                                                                                 page = 1,
                                                                                 size = 6,
                                                                                 sort = "createdAt",
                                                                                 direction = Sort.Direction.DESC)
                                                                         Pageable pageable);
}
