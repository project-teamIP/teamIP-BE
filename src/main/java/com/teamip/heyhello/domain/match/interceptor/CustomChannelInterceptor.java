package com.teamip.heyhello.domain.match.interceptor;

import com.teamip.heyhello.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public CustomChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {

            if(!jwtUtil.validateToken(headerAccessor.getNativeHeader("Authorization").get(0))){
                log.info("토큰 에러입니다. 조심하세요");
            }
            return message;
        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            return message;
        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            return message;
        }
        return message;
    }
}
