package com.teamip.heyhello.domain.socketio.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.teamip.heyhello.domain.match.service.IoMatchService;
import com.teamip.heyhello.global.redis.WaitUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketDisconnectController {
    private final WaitUserRepository waitUserRepository;
    private final IoMatchService ioMatchService;
    private final SocketIOServer socketIOServer;

    @PostConstruct
    public void initializeDisconnectListener() {
        socketIOServer.addDisconnectListener(this::onDisconnect);
    }
    @Transactional
    public void onDisconnect(SocketIOClient client) {
        waitUserRepository.removeWaitUserAtList(client);
    }

}
