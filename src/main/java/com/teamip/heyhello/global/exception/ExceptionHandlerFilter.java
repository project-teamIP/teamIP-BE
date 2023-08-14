package com.teamip.heyhello.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다.");
        } catch(JwtException | IllegalArgumentException e) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");
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
        String responseString = new ObjectMapper().writeValueAsString(payload);
        response.getWriter().write(responseString);
    }
}
