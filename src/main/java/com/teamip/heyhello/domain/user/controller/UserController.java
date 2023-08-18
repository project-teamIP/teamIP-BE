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
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import com.teamip.heyhello.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final BuddyService buddyService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestHeader("AccessToken") String atk) {
        StatusResponseDto response = userService.withdrawal(userDetails, atk);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDto> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(userService.getMypage(userDetails));
    }

    @PatchMapping
    public ResponseEntity<StatusResponseDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody UpdateProfileDto updateProfileDto) {

        return ResponseEntity.ok(userService.updateProfile(userDetails, updateProfileDto));
    }


    @GetMapping("/check")
    public ResponseEntity<StatusResponseDto> checkDuplicated(@RequestParam(required = false) String email,
                                                             @RequestParam(required = false) String nickname) {

        return ResponseEntity.ok().body(userService.checkDuplicated(email, nickname));
    }


    @PostMapping("/buddy/{nickname}")
    public ResponseEntity<StatusResponseDto> addBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable String nickname) {

        return ResponseEntity.ok(buddyService.addBuddy(userDetails, nickname));
    }

    @GetMapping("/buddy")
    public ResponseEntity<Page<BuddyResponseDto>> getBuddies(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PageableDefault(page = 1, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(buddyService.getBuddies(userDetails, pageable));
    }

    @DeleteMapping("/buddy/{nickname}")
    public ResponseEntity<StatusResponseDto> deleteBuddy(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable String nickname) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(buddyService.deleteBuddy(userDetails, nickname));
    }

    @PutMapping("/image")
    public ResponseEntity<UrlResponseDto> modifyProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        return ResponseEntity.ok(userService.modifyProfileImage(userDetails, image));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return kakaoService.kakaoLogin(code);
    }

    @GetMapping("/login/google")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return googleService.googleLogin(code);
    }

    @GetMapping("/auth/social")
    public ResponseEntity<StatusResponseDto> getKakaoTokenForWithdrawal(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException{

        return kakaoService.getKakaoTokenForWithdrawal(code);
    }
    @PostMapping("/withdrawal/social")
    public ResponseEntity<StatusResponseDto> withdrawal(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam String provider,
                                                        @RequestParam String token,
                                                        @RequestHeader("AccessToken") String atk) {
        if ("kakao".equals(provider)) {
            return ResponseEntity.ok(kakaoService.kakaoWithdrawal(userDetails, token, atk));
        }
//        if ("google".equals(provider)) {
//        //    return googleService.googleWithdrawal(userDetails, accessToken);
 //       }
        else {
            throw new IllegalArgumentException("유효한 provider가 아닙니다.");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ActiveUserResponseDto> countActiveUser(){
        return ResponseEntity.ok().body(new ActiveUserResponseDto(userService.countActiveUser()));
    }

    @PutMapping("/point")
    public ResponseEntity<StatusResponseDto> rateCleanPoint(@RequestHeader("AccessToken") String tokenValue,
                                                            @RequestBody CleanPointRequestDto requestDto){
        User user = jwtUtil.getUserFromToken(tokenValue);
        return ResponseEntity.ok().body(userService.rateCleanPoint(user, requestDto.getPartnerNickname(), requestDto.getPoint()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardResponseDto> getDashBoardInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userService.getDashBoardInfo(userDetails));
    }
}
