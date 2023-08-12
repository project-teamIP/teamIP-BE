package com.teamip.heyhello.domain.socketio.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.socketio.dto.IceDto;
import com.teamip.heyhello.domain.socketio.dto.OfferDto;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IoMessageService {

    private final ObjectMapper objectMapper;

    public void sendOfferMessage(SocketIOServer server, String message) throws JsonProcessingException {
        OfferDto offerDto = objectMapper.readValue(message, OfferDto.class);
        server.getRoomOperations(offerDto.getRoomId()).sendEvent(SocketProperty.OFFER_KEY, offerDto);
    }

    public void sendAnswerMessage(SocketIOServer server, String message) throws JsonProcessingException {
        OfferDto offerDto = objectMapper.readValue(message, OfferDto.class);
        server.getRoomOperations(offerDto.getRoomId()).sendEvent(SocketProperty.ANSWER_KEY, offerDto);
    }

    public void sendIceMessage(SocketIOServer server, String message) throws JsonProcessingException {
        IceDto iceDto = objectMapper.readValue(message, IceDto.class);
        server.getRoomOperations(iceDto.getRoomId()).sendEvent(SocketProperty.ICE_KEY, iceDto);
    }

    public void sendIceAnswerMessage(SocketIOServer server, String message) throws JsonProcessingException {
        IceDto iceDto = objectMapper.readValue(message, IceDto.class);
        server.getRoomOperations(iceDto.getRoomId()).sendEvent("iceanswer", iceDto);
    }
}

