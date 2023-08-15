package com.teamip.heyhello.domain.socketio.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class SocketProperties {
    @Value("${socket.port}")
    private int port;

}
