package com.teamip.heyhello.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.service.GoogleService;
import com.teamip.heyhello.domain.user.service.KakaoService;
import com.teamip.heyhello.domain.user.service.UserService;
import com.teamip.heyhello.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@RequestParam Long userId){

        return ResponseEntity.ok(userService.withdrawal(userId));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<StatusResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        String token = kakaoService.kakaoLogin(code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        return ResponseEntity.ok().headers(headers).body(StatusResponseDto.builder().status(HttpStatus.OK).message("로그인 성공@#!@#!@ㄲ!#토큰 가져가세요").build());
    }

    @GetMapping("/login/google")
    public ResponseEntity<StatusResponseDto> googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        String token = googleService.googleLogin(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        return ResponseEntity.ok().headers(headers).body(StatusResponseDto.builder().status(HttpStatus.OK).message("로그인 성공@#!@#!@ㄲ!#토큰 가져가세요").build());
    }
}
