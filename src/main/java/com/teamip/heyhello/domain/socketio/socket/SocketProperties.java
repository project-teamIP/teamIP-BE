package com.teamip.heyhello.domain.socketio.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class SocketProperties {

    private int port = 5004;

}
