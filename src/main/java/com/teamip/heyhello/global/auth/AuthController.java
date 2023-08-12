package com.teamip.heyhello.global.auth;

import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @GetMapping("/auth/re-access")
    public ResponseEntity<StatusResponseDto> getAccessTokenByRefreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @RequestHeader("RefreshToken") String rtk,
                                                                          HttpServletResponse response) {

        return ResponseEntity.ok(tokenService.getAtkByRtk(userDetails, rtk, response));
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<StatusResponseDto> logoutWithToken(@RequestHeader("AccessToken") String atk,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(tokenService.logoutWithAtk(atk, userDetails));
    }

    @GetMapping("/api/rtk-list")
    public String printRtkList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return tokenService.print(userDetails.getUsername());
    }
}
