package com.teamip.heyhello.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.TokenService;
import com.teamip.heyhello.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, TokenService tokenService, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getSubstringTokenFromRequest(request);
        if (token != null) {
            boolean isAuthenticated = tryAuthentication(token, request, response);
            if (isAuthenticated) {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean tryAuthentication(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (tokenService.isBlackList(token)) {
            StatusResponseDto statusResponseDto =
                    StatusResponseDto.builder()
                            .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                            .message("로그아웃된 토큰입니다.")
                            .build();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            String responseString = new ObjectMapper().writeValueAsString(statusResponseDto);
            response.getWriter().write(responseString);
            return false;
        }

        Claims userInfo;
        String email;

        if (request.getRequestURI().equals("/auth/re-access")) {
            userInfo = jwtUtil.getUserInfoFromTokenWithoutValidate(token);
            email = userInfo.getSubject();
        } else {
            userInfo = jwtUtil.getUserInfoFromToken(token);
            email = userInfo.getSubject();
        }

        try {
            setAuthentication(email);
        } catch (Exception e) {
            throw new RuntimeException("SecurityContextHolder에 Authentication 등록 실패");
        }
        return true;
    }

    private void setAuthentication(String loginId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(loginId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String loginId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);

    }
}
