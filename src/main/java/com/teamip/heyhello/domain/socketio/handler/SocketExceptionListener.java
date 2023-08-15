package com.teamip.heyhello.domain.socketio.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketExceptionListener implements ExceptionListener {

    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
        client.disconnect();
    }

    @Override
    public void onPingException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public void onPongException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        return false;
    }

    private void runExceptionHandling(Exception e, SocketIOClient client) {
        final StatusResponseDto message;

        if (e instanceof RuntimeException) {
           RuntimeException runtimeException = (RuntimeException) e;

            message = StatusResponseDto.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else if(e.getCause() instanceof RuntimeException) {
            RuntimeException runtimeException = (RuntimeException)e.getCause();

            message = StatusResponseDto.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            e.printStackTrace();
            message = StatusResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Internal Server Error")
                    .build();
        }

        client.sendEvent(SocketProperty.ERROR_KEY, message);

    }

}
