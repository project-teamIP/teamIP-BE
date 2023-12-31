package com.teamip.heyhello.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teamip.heyhello.domain.user.dto.*;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.service.BuddyService;
import com.teamip.heyhello.domain.user.service.GoogleService;
import com.teamip.heyhello.domain.user.service.KakaoService;
import com.teamip.heyhello.domain.user.service.UserService;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserSwaggerController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final BuddyService buddyService;
    private final JwtUtil jwtUtil;
    @Override
    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }
    @PostMapping("/rejoin")
    public ResponseEntity<StatusResponseDto> rejoin(@RequestParam String loginId) {
        StatusResponseDto response = userService.rejoin(loginId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestHeader("AccessToken") String atk) {
        StatusResponseDto response = userService.withdrawal(userDetails, atk);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDto> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(userService.getMypage(userDetails));
    }
    @Override
    @PatchMapping
    public ResponseEntity<StatusResponseDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UpdateProfileDto updateProfileDto) {

        return ResponseEntity.ok(userService.updateProfile(userDetails, updateProfileDto));
    }

    @Override
    @GetMapping("/check")
    public ResponseEntity<StatusResponseDto> checkDuplicated(@RequestParam(required = false) String email,
                                                             @RequestParam(required = false) String nickname) {

        return ResponseEntity.ok().body(userService.checkDuplicated(email, nickname));
    }

    @Override
    @PostMapping("/buddy/{nickname}")
    public ResponseEntity<StatusResponseDto> addBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable String nickname) {

        return ResponseEntity.ok(buddyService.addBuddy(userDetails, nickname));
    }
    @Override
    @GetMapping("/buddy")
    public ResponseEntity<Page<BuddyResponseDto>> getBuddies(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PageableDefault(page = 1, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(buddyService.getBuddies(userDetails, pageable));
    }
    @Override
    @DeleteMapping("/buddy/{nickname}")
    public ResponseEntity<StatusResponseDto> deleteBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable String nickname) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(buddyService.deleteBuddy(userDetails, nickname));
    }
    @Override
    @PutMapping("/image")
    public ResponseEntity<UrlResponseDto> modifyProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "profile", required = false)String profile) throws IOException {
        return ResponseEntity.ok(userService.modifyProfileImage(userDetails, image, profile));
    }
    @Override
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return kakaoService.kakaoLogin(code);
    }
    @Override
    @GetMapping("/login/google")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return googleService.googleLogin(code);
    }
    @Override
    @GetMapping("/auth/social")
    public ResponseEntity<StatusResponseDto> getKakaoTokenForWithdrawal(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return kakaoService.getKakaoTokenForWithdrawal(code);
    }
    @PostMapping("/reactive/social")
    public ResponseEntity<StatusResponseDto> reactiveSocialUser(@RequestParam String provider,
                                                                @RequestParam String loginId) {
        ResponseEntity<StatusResponseDto> responseEntity;

        if ("kakao".equals(provider)) {
            responseEntity = kakaoService.reactiveKakaoUser(loginId);
        } else if ("google".equals(provider)) {
            responseEntity = googleService.reactiveGoogleUser(loginId);
        } else {
            throw new IllegalArgumentException("유효한 provider가 아닙니다.");
        }

        return responseEntity;
    }
    @Override
    @PostMapping("/withdrawal/social")
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam String provider,
                                                        @RequestParam String token,
                                                        @RequestHeader("AccessToken") String atk) {
        if ("kakao".equals(provider)) {
            return ResponseEntity.ok(kakaoService.kakaoWithdrawal(userDetails, token, atk));
        }
        else if ("google".equals(provider)) {
            return ResponseEntity.ok(googleService.googleWithdrawal(userDetails, token, atk));
        }
        else {
            throw new IllegalArgumentException("유효한 provider가 아닙니다.");
        }
    }
    @Override
    @GetMapping("/count")
    public ResponseEntity<ActiveUserResponseDto> countActiveUser(){
        return ResponseEntity.ok().body(new ActiveUserResponseDto(userService.countActiveUser()));
    }
    @Override
    @PutMapping("/point")
    public ResponseEntity<StatusResponseDto> rateCleanPoint(@RequestHeader("AccessToken") String tokenValue,
                                                            @RequestBody CleanPointRequestDto requestDto){
        User user = jwtUtil.getUserFromToken(tokenValue);
        return ResponseEntity.ok().body(userService.rateCleanPoint(user, requestDto.getPartnerNickname(), requestDto.getPoint()));
    }
    @Override
    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardResponseDto> getDashBoardInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userService.getDashBoardInfo(userDetails));
    }
    @Override
    @GetMapping("/test")
    public ResponseEntity<DashBoardResponseDto> getDashBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getDashBordData(userDetails));
    }
}
