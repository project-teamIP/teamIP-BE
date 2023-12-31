package com.teamip.heyhello.global.redis;

import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String generateRefreshToken(String loginId) {

        return refreshTokenRepository.createAndSave(loginId);
    }
    public String renewRefreshToken(String loginId, String refreshToken) {

        return refreshTokenRepository.renewAndSave(loginId, refreshToken);
    }


    public String generateAccessToken(String loginId) {

        return jwtUtil.createAccessToken(loginId);
    }
    public String getRefreshToken(String loginId){
        return refreshTokenRepository.findRefreshTokenByLoginId(loginId);
    }
    public StatusResponseDto getAtkByRtk(UserDetailsImpl userDetails, String rtk, HttpServletResponse response) {
        String loginId = userDetails.getUsername();
        RefreshToken refreshToken = refreshTokenRepository.findByLoginIdAndRefreshToken(loginId, rtk).orElseThrow(
                () -> new RuntimeException("발급된 적 없거나 만료된 토큰입니다")
        );
        if (!loginId.equals(refreshToken.getLoginId())) {
            throw new RuntimeException("타인의 토큰은 사용할 수 없습니다.");
        }

        String generatedAtk = generateAccessToken(loginId);
        response.addHeader("AccessToken", generatedAtk);

        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message("AccessToken 발행 성공")
                .build();
    }
    public String createOrRenewRefreshToken(String loginId) {
        String refreshToken = getRefreshToken(loginId);
        if(refreshToken==null) {
            refreshToken = generateRefreshToken(loginId);
        } else{
            renewRefreshToken(loginId, refreshToken);
        }
        return refreshToken;
    }
    public StatusResponseDto logoutWithAtk(String atk, UserDetailsImpl userDetails) {
        String loginId = userDetails.getUsername();
        refreshTokenRepository.deleteByRefreshToken(loginId);

        BlackListJwt blackListJwt = new BlackListJwt(loginId, atk);
        blackListRepository.setBlackList(blackListJwt);

        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message("로그아웃 성공")
                .build();
    }

    public boolean isBlackList(String atk) {
        Optional<BlackListJwt> optionalJwt = blackListRepository.getBlackList(atk);

        return optionalJwt.isPresent();
    }

    public String print(String loginId) {
        return refreshTokenRepository.printHashOpsByLoginId(loginId);
    }

    public void invalidateTokensForUser(User user,String atk) {
        blackListRepository.setBlackList(new BlackListJwt(user.getLoginId(), atk));

        refreshTokenRepository.deleteByRefreshToken(user.getLoginId());
    }
}
