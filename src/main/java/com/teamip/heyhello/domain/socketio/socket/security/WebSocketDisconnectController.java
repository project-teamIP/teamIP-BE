package com.teamip.heyhello.domain.socketio.socket.security;

import com.corundumstudio.socketio.SocketIOClient;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketDisconnectController {

    @Transactional
    public void onDisConnect(SocketIOClient client) {
        log.info("onDisconnect 발동@@@@@@@@@@@@@@@@");
        client.disconnect();
    }

}
