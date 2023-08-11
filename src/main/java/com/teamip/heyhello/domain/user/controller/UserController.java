package com.teamip.heyhello.domain.user.controller;

import com.teamip.heyhello.domain.user.dto.*;
import com.teamip.heyhello.domain.user.service.BuddyService;
import com.teamip.heyhello.domain.user.service.UserService;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
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
public class UserController {

    private final UserService userService;
    private final BuddyService buddyService;
//    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@RequestParam Long userId) {

        return ResponseEntity.ok(userService.withdrawal(userId));
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
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        return ResponseEntity.ok(userService.modifyProfileImage(userDetails, image));
    }
}
