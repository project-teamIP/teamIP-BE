package com.teamip.heyhello.global.redis;

import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository repository;


    public String generateRefreshToken(String loginId) {
        RefreshToken rtk = new RefreshToken(UUID.randomUUID().toString(), loginId);
        repository.save(rtk);

        return rtk.getRefreshToken();
    }

    public String generateAccessToken(String loginId) {

        return jwtUtil.createAccessToken(loginId);
    }

    public StatusResponseDto getAtkByRtk(UserDetailsImpl userDetails, String rtk, HttpServletResponse response) {
        String loginId = userDetails.getUsername();
        RefreshToken refreshToken = repository.findByRefreshToken(rtk).orElseThrow(
                () -> new RuntimeException("발급된 적 없거나 만료된 토큰입니다")
        );
        if (!loginId.equals(refreshToken.getLoginId())) {
            throw new RuntimeException("타인의 토큰은 사용할 수 없습니다.");
        }

        String generatedAtk = generateAccessToken(loginId);
        response.addHeader("RefreshToken", generatedAtk);

        return StatusResponseDto
                .builder()
                .status(HttpStatus.OK)
                .message("AccessToken 발행 성공")
                .build();
    }
}
