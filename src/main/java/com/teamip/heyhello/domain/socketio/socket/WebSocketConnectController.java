package com.teamip.heyhello.domain.socketio.socket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketConnectController {


    public void onConnect(SocketIOClient client) {

    }

}
