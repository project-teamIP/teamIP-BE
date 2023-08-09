package com.teamip.heyhello.domain.user.controller;

import com.teamip.heyhello.domain.user.dto.MypageResponseDto;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.UrlResponseDto;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.dto.UpdateUserInfoDto;
import com.teamip.heyhello.domain.user.service.UserService;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
//    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@RequestParam Long userId){

        return ResponseEntity.ok(userService.withdrawal(userId));
    }

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDto> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(userService.getMypage(userDetails));
    }

    @PostMapping("/info")
    public ResponseEntity<StatusResponseDto> initRemainingUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateUserInfoDto updateUserInfoDto) {

        return ResponseEntity.ok(userService.initRemainingUserInfo(userDetails, updateUserInfoDto));
    }

//    @GetMapping("/login/kakao")
//    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
//        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
//        String token = kakaoService.kakaoLogin(code);
//
//        // Cookie 생성 및 직접 브라우저에 Set
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return "redirect:/";
//    }
    @PutMapping("/image")
    public ResponseEntity<UrlResponseDto> modifyProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "image",required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(userService.modifyProfileImage(userDetails, image));
    }
}
