package com.teamip.heyhello.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.user.dto.LoginRequestDto;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.util.JwtUtil;
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

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
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
        // getUsername == getLoginId(email or id)
        String token = jwtUtil.createToken(((UserDetailsImpl) authResult.getPrincipal()).getUsername());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, "로그인 성공");
        returnStatusResponse(response, responseDto, HttpStatus.OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());


        StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.UNAUTHORIZED, failed.getMessage());
        returnStatusResponse(response, responseDto, HttpStatus.UNAUTHORIZED);
    }

    private void isUserBlocked(String loginId) throws IOException, IllegalAccessException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new IOException("")
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

    private void returnStatusResponse(HttpServletResponse response, StatusResponseDto statusResponseDto, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(httpStatus.value());
        String responseString = objectMapper.writeValueAsString(statusResponseDto);
        response.getWriter().write(responseString);
    }
}
