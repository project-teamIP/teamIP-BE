package com.teamip.heyhello.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teamip.heyhello.domain.user.dto.*;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TestController extends SwaggerController {
    @Operation(summary = "회원 가입", description = "이메일을 통한 회원 가입 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto);


    @Operation(summary = "회원 탈퇴", description = "이메일을 통한 회원 탈퇴 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestHeader("AccessToken") String atk);


    @Operation(summary = "마이 페이지 본인 정보", description = "마이 페이지 본인 정보 요청 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MypageResponseDto.class)),})
    })
    public ResponseEntity<MypageResponseDto> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "프로필 수정", description = "프로필 수정 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UpdateProfileDto updateProfileDto);


    @Operation(summary = "이메일, 닉네임 중복확인", description = "가입 시 email, 가입 & 수정 시 nickname 중복 확인 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> checkDuplicated(@RequestParam(required = false) String email,
                                                             @RequestParam(required = false) String nickname);

    @Operation(summary = "친구 추가", description = "친구 추가 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)),})
    })
    public ResponseEntity<StatusResponseDto> addBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable String nickname);

    @Operation(summary = "구글 로그인", description = "구글 로그인 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = BuddyResponseDto.class)) })
    })
    public ResponseEntity<Page<BuddyResponseDto>> getBuddies(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PageableDefault(page = 1, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);
    @Operation(summary = "친구 삭제", description = "친구 삭제 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class)) })
    })
    public ResponseEntity<StatusResponseDto> deleteBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable String nickname);

    @Operation(summary = "프로필 이미지 수정", description = "프로필 이미지 수정 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = UrlResponseDto.class))})
    })
    public ResponseEntity<UrlResponseDto> modifyProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = LoginResponseDto.class))})
    })
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException;

    @Operation(summary = "구글 로그인", description = "구글 로그인 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = LoginResponseDto.class))})
    })
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException;

    @Operation(summary = "탈퇴를 위한 카카오 로그인", description = "탈퇴 본인 확인을 위한 카카오 로그인 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class))})
    })
    public ResponseEntity<StatusResponseDto> getKakaoTokenForWithdrawal(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException;

    @Operation(summary = "소셜로그인 유저 탈퇴", description = "소셜 로그인 유저의 회원 탈퇴ㅓ api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class))})
    })
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam String provider,
                                                        @RequestParam String token,
                                                        @RequestHeader("AccessToken") String atk);

    @Operation(summary = "통화 종료 후 점수 매기기", description = "통화가 종료된 후 상대방의 매너 점수를 매기는 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class))})
    })
    public ResponseEntity<StatusResponseDto> rateCleanPoint(@RequestHeader("AccessToken") String tokenValue,
                                                            @RequestBody CleanPointRequestDto requestDto);

    @Operation(summary = "현재 접속자 수 조회", description = "현재 활동 중인 접속자 수 조회 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ActiveUserResponseDto.class)),})
    })
    public ResponseEntity<ActiveUserResponseDto> countActiveUser();

    @Operation(summary = "대시보드 정보", description = "로그인 후 나오는 대시보드 정보 요청 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = DashBoardResponseDto.class)),})
    })
    public ResponseEntity<DashBoardResponseDto> getDashBoardInfo(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "대시보드 정보test", description = "로그인 후 나오는 대시보드 정보 요청 test api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = DashBoardResponseDto.class)),})
    })
    public ResponseEntity<DashBoardResponseDto> getDashBoard(@AuthenticationPrincipal UserDetailsImpl userDetails);
}