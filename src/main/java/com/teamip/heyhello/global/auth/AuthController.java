package com.teamip.heyhello.global.auth;

import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @GetMapping("/refresh-token")
    public ResponseEntity<StatusResponseDto> getAccessTokenByRefreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestHeader("RefreshToken") String rtk,
                                                             HttpServletResponse response) {

        return ResponseEntity.ok(tokenService.getAtkByRtk(userDetails, rtk, response));
    }
}
