package com.teamip.heyhello.domain.socketio.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.socketio.annotation.SocketController;
import com.teamip.heyhello.domain.socketio.annotation.SocketMapping;
import com.teamip.heyhello.domain.socketio.dto.IceDto;
import com.teamip.heyhello.domain.socketio.dto.OfferDto;
import com.teamip.heyhello.domain.socketio.dto.RequestUserDto;
import com.teamip.heyhello.domain.socketio.service.IoMatchService;
import com.teamip.heyhello.domain.socketio.service.IoMessageService;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ObjectMapper objectMapper;
    private final IoMatchService ioMatchService;
    private final IoMessageService ioMessageService;

    @SocketMapping(endpoint = "match", requestCls = String.class)
    public void findMatch(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException {
        RequestUserDto requestUserDto = objectMapper.readValue(message, RequestUserDto.class);
        ioMatchService.findMatch(server, client, requestUserDto);
    }

    @SocketMapping(endpoint = SocketProperty.OFFER_KEY, requestCls = String.class)
    public void sendOfferMessage(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException {
        ioMessageService.sendOfferMessage(server, message);
    }

    @SocketMapping(endpoint = SocketProperty.ANSWER_KEY, requestCls = String.class)
    public void sendAnswerMessage(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException {
        ioMessageService.sendAnswerMessage(server, message);
    }


    @SocketMapping(endpoint = SocketProperty.ICE_KEY, requestCls = String.class)
    public void sendIceMessage(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException{
        ioMessageService.sendIceMessage(server, message);
    }



    @SocketMapping(endpoint = "iceanswer", requestCls = String.class)
    public void sendIceAnswerMessage(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException{
        ioMessageService.sendIceAnswerMessage(server, message);
    }


}
