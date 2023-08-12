package com.teamip.heyhello.domain.socketio.socket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketConnectController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public void onConnect(SocketIOClient client) {
        log.info("onConnect 발동@@@@@@@@@@@@");
   //     String token = client.getHandshakeData().getSingleUrlParam("authorization");
 //       client.set(AuthenticationProperty.USER_KEY, jwtUtil.validateToken(token));
   //     userRepository.findById(client.get(AuthenticationProperty.USER_KEY)).orElse(null);
    }

}
