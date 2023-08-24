package com.teamip.heyhello.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.user.dto.LoginRequestDto;
import com.teamip.heyhello.domain.user.dto.LoginResponseDto;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.redis.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;


    public JwtAuthenticationFilter(TokenService tokenService, ObjectMapper objectMapper, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
            isUserBlocked(loginRequestDto.getLoginId());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "잘못된 입력입니다.");
        } catch (IllegalAccessException e) {
            sendErrorResponse(response, HttpStatus.FORBIDDEN, "사용이 정지된 계정입니다.");
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String loginId = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String accessToken = tokenService.generateAccessToken(loginId);
        String refreshToken = createOrRenewRefreshToken(loginId);

        response.setHeader("AccessToken", accessToken);
        response.setHeader("RefreshToken", refreshToken);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LoginResponseDto responseDto = new LoginResponseDto(user);

        returnStatusResponse(response, responseDto, HttpStatus.OK);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());


        StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.UNAUTHORIZED, failed.getMessage());
        returnStatusResponse(response, responseDto, HttpStatus.UNAUTHORIZED);
    }

    private String createOrRenewRefreshToken(String loginId) {
        String refreshToken = tokenService.getRefreshToken(loginId);
        if(refreshToken==null) {
            refreshToken = tokenService.generateRefreshToken(loginId);
        } else{
            tokenService.renewRefreshToken(loginId, refreshToken);
        }
        return refreshToken;
    }
    private void isUserBlocked(String loginId) throws IOException, IllegalAccessException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new IOException()
        );
        if(Boolean.TRUE.equals(user.getIsLocked())) {
            throw new IllegalAccessException();
        }

    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message) {
        StatusResponseDto responseDto = new StatusResponseDto(httpStatus, message);
        try {
            returnStatusResponse(response, responseDto, HttpStatus.UNAUTHORIZED);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private<T> void returnStatusResponse(HttpServletResponse response, T payload, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(httpStatus.value());
        String responseString = objectMapper.writeValueAsString(payload);
        response.getWriter().write(responseString);
    }
}
