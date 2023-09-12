package com.teamip.heyhello.domain.match.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.match.dto.*;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoSignalService {

    public void sendOfferMessage(SocketIOServer server, SocketIOClient client, OfferAndAnswerDto message) throws JsonProcessingException {
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.OFFER_KEY, client, message);
    }

    public void sendAnswerMessage(SocketIOServer server, SocketIOClient client, OfferAndAnswerDto message) throws JsonProcessingException {
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ANSWER_KEY, client, message);
    }

    public void sendIceMessage(SocketIOServer server, SocketIOClient client, CandidateDto message) throws JsonProcessingException {
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ICE_KEY, client, message);
    }

}

