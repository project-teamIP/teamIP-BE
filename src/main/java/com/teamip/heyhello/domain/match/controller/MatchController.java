package com.teamip.heyhello.domain.match.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.match.dto.CandidateDto;
import com.teamip.heyhello.domain.match.dto.MatchUserInfoDto;
import com.teamip.heyhello.domain.match.dto.OfferAndAnswerDto;
import com.teamip.heyhello.domain.socketio.annotation.SocketController;
import com.teamip.heyhello.domain.socketio.annotation.SocketMapping;
import com.teamip.heyhello.domain.match.dto.RequestUserDto;
import com.teamip.heyhello.domain.match.service.IoMatchService;
import com.teamip.heyhello.domain.match.service.IoSignalService;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    private final ObjectMapper objectMapper;
    private final IoMatchService ioMatchService;
    private final IoSignalService ioSignalService;

    @SocketMapping(endpoint = SocketProperty.MATCH_KEY, requestCls = RequestUserDto.class)
    public void findMatch(SocketIOClient client, SocketIOServer server, RequestUserDto message) throws JsonProcessingException {
        ioMatchService.findMatch(server, client, message);
    }

    @SocketMapping(endpoint = SocketProperty.OFFER_KEY, requestCls = OfferAndAnswerDto.class)
    public void sendOfferMessage(SocketIOClient client, SocketIOServer server, OfferAndAnswerDto message) throws JsonProcessingException {
        ioSignalService.sendOfferMessage(server, client, message);
    }

    @SocketMapping(endpoint = SocketProperty.ANSWER_KEY, requestCls = OfferAndAnswerDto.class)
    public void sendAnswerMessage(SocketIOClient client, SocketIOServer server, OfferAndAnswerDto message) throws JsonProcessingException {
       ioSignalService.sendAnswerMessage(server, client, message);
    }


    @SocketMapping(endpoint = SocketProperty.ICE_KEY, requestCls = CandidateDto.class)
    public void sendIceMessage(SocketIOClient client, SocketIOServer server, CandidateDto message) throws JsonProcessingException{
        if(message!=null && !message.equals("null")) {
            ioSignalService.sendIceMessage(server, client, message);
        }
    }
    @SocketMapping(endpoint = SocketProperty.CANCEL_KEY, requestCls = String.class)
    public void cancelFindMatch(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException{
        ioMatchService.cancelFindMatch(server, client, message);
    }

    @SocketMapping(endpoint = SocketProperty.ENDCALL_KEY, requestCls = String.class)
    public void endCall(SocketIOClient client, SocketIOServer server, String message) throws JsonProcessingException{
        ioMatchService.endCall(server, client);
    }
    @SocketMapping(endpoint = SocketProperty.MATCH_USER_INFO_KEY, requestCls = MatchUserInfoDto.class)
    public void sendMatchUserInfo(SocketIOClient client, SocketIOServer server, MatchUserInfoDto message) throws JsonProcessingException{
        ioMatchService.sendMatchUserInfo(server, client, message);
    }
    @SocketMapping(endpoint = SocketProperty.ANSWER_USER_INFO_KEY, requestCls = MatchUserInfoDto.class)
    public void sendAnswerUserInfo(SocketIOClient client, SocketIOServer server, MatchUserInfoDto message) throws JsonProcessingException{
        ioMatchService.sendAnswerUserInfo(server, client, message);
    }
}
