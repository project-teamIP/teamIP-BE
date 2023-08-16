package com.teamip.heyhello.domain.socketio.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.socketio.dto.*;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoMessageService {

    private final ObjectMapper objectMapper;

    public void sendOfferMessage(SocketIOServer server, SocketIOClient client, String message) throws JsonProcessingException {
        OfferAndAnswerDto offer = objectMapper.readValue(message, OfferAndAnswerDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.OFFER_KEY, client, offer);
    }

    public void sendAnswerMessage(SocketIOServer server, SocketIOClient client, String message) throws JsonProcessingException {
        OfferAndAnswerDto answer = objectMapper.readValue(message, OfferAndAnswerDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ANSWER_KEY, client, answer);
    }

    public void sendIceMessage(SocketIOServer server, SocketIOClient client, String message) throws JsonProcessingException {
        CandidateDto candidateDto = objectMapper.readValue(message, CandidateDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ICE_KEY, client, candidateDto);
    }
}

