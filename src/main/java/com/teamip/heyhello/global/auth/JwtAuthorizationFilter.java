package com.teamip.heyhello.global.auth;

import com.teamip.heyhello.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this. userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getSubstringTokenFromRequest(request);
        if(token!=null) {
            tryAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private void tryAuthentication(String token) {
        if (!jwtUtil.validateToken(token)) {
            logger.error("valid token failed");
            return;
        }

        Claims userInfo = jwtUtil.getUserInfoFromToken(token);
        String email = userInfo.getSubject();

        try {
            setAuthentication(email);
        } catch (Exception e) {
            throw new RuntimeException("SecurityContextHolder에 Authentication 등록 실패");
        }
    }

    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);

    }
}
