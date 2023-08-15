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
//        OfferRequestDto offerRequestDto = objectMapper.readValue(message, OfferRequestDto.class);
//        OfferAndAnswerDto offerAndAnswerDto = OfferAndAnswerDto.builder().type(offerRequestDto.getType()).sdp(offerRequestDto.getSdp()).build();
//        server.getRoomOperations(offerRequestDto.getRoomId()).sendEvent(SocketProperty.OFFER_KEY, client ,OfferResponseDto.builder()
//                .roomId(offerRequestDto.getRoomId())
//                .nickname(offerRequestDto.getNickname())
//                .country(offerRequestDto.getCountry())
//                .userId(offerRequestDto.getUserId())
//                .offer(offerAndAnswerDto)
//                .build());
        OfferAndAnswerDto offer = objectMapper.readValue(message, OfferAndAnswerDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.OFFER_KEY, client, offer);
    }

    public void sendAnswerMessage(SocketIOServer server, SocketIOClient client, String message) throws JsonProcessingException {
//        AnswerRequestDto answerRequestDto = objectMapper.readValue(message, AnswerRequestDto.class);
//        OfferAndAnswerDto answerDto = OfferAndAnswerDto.builder().type(answerRequestDto.getType()).sdp(answerRequestDto.getSdp()).build();
//        server.getRoomOperations(answerRequestDto.getRoomId()).sendEvent(SocketProperty.ANSWER_KEY, client, AnswerResponseDto.builder()
//                .userId(answerRequestDto.getUserId())
//                .answer(answerDto)
//                .build());
        OfferAndAnswerDto answer = objectMapper.readValue(message, OfferAndAnswerDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ANSWER_KEY, client, answer);
    }

    public void sendIceMessage(SocketIOServer server, SocketIOClient client, String message) throws JsonProcessingException {
        CandidateDto candidateDto = objectMapper.readValue(message, CandidateDto.class);
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ICE_KEY, client, candidateDto);
    }
}

