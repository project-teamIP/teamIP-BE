package com.teamip.heyhello.domain.socketio.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.teamip.heyhello.domain.socketio.handler.SocketExceptionListener;
import com.teamip.heyhello.domain.socketio.socket.SocketProperties;
import com.teamip.heyhello.domain.socketio.socket.WebsocketAddMappingSupporter;
import com.teamip.heyhello.domain.socketio.socket.WebSocketConnectController;
import com.teamip.heyhello.domain.socketio.socket.WebSocketDisconnectController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketConfig {

    private final WebsocketAddMappingSupporter mappingSupporter;
    private final WebSocketConnectController connectController;
    private final WebSocketDisconnectController disConnectController;
    private final SocketExceptionListener exceptionListener;
    private final SocketProperties socketProperties;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(socketProperties.getPort());
        config.setOrigin("*");
        config.setExceptionListener(exceptionListener);
        SocketIOServer server = new SocketIOServer(config);
        mappingSupporter.addListeners(server);
        server.addConnectListener(connectController::onConnect);
        server.addDisconnectListener(disConnectController::onDisConnect);
        return server;
    }

}
