package com.teamip.heyhello.domain.match.service;

import com.teamip.heyhello.domain.match.dto.RoomStatusDto;
import com.teamip.heyhello.domain.match.dto.MatchInfoRequestDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageService {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final UserRepository userRepository;

    public MessageService(SimpMessageSendingOperations simpMessageSendingOperations, UserRepository userRepository) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.userRepository = userRepository;
    }

    public MatchInfoRequestDto parseHeadersInfo(StompHeaderAccessor headerAccessor) {
        Long userId = Long.valueOf(headerAccessor.getNativeHeader("userId").get(0));
        User user = userRepository.findById(userId).orElseThrow(()->new NullPointerException("유저 정보가 잘못되었습니다. 요청을 다시 확인해주세요."));

        if(user!=null){
            log.info("대기열에 참가할 user = " + user.getNickname());
        }
        return MatchInfoRequestDto.builder()
                .userEndpoint(headerAccessor.getNativeHeader("path").get(0))
                .userLanguage(headerAccessor.getNativeHeader("userLanguage").get(0))
                .targetLanguage(headerAccessor.getNativeHeader("targetLanguage").get(0))
                .user(user)
                .build();
    }

    public void SendSearchResult(String endpoint, RoomStatusDto roomStatusDto) {

        if (roomStatusDto != null) {
            sendMatchInfoToEachUser(endpoint, roomStatusDto);
        } else {
            sendWaitMessageToRequestUser(endpoint);
        }
    }

    private void sendWaitMessageToRequestUser(String endpoint) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", false);
        simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "현재 매칭 가능한 유저가 없습니다. 조금만 더 기다려주세요.", header);
    }

    private void sendMatchInfoToEachUser(String endpoint, RoomStatusDto roomStatusDto) {
        sendSuccessMessageToRequestUser(endpoint, roomStatusDto);
        sendSuccessMessageToWaitUser(roomStatusDto);
    }

    private void sendSuccessMessageToRequestUser(String endpoint, RoomStatusDto roomStatusDto) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", true);
        header.put("path", roomStatusDto.getUser1Endpoint());
        header.put("user", roomStatusDto.getUser2().getNickname());
        simpMessageSendingOperations.convertAndSend("/match/" + endpoint, "매칭이 즉시 가능합니다. 헤더의 path를 endpoint로 연결 정보를 전송합니다.", header);
    }
    private void sendSuccessMessageToWaitUser(RoomStatusDto roomStatusDto) {
        Map<String, Object> header = new HashMap<>();
        header.put("isMatch", true);
        header.put("path", roomStatusDto.getUser2Endpoint());
        header.put("user", roomStatusDto.getUser1().getNickname());
        simpMessageSendingOperations.convertAndSend("/match/" + roomStatusDto.getUser1Endpoint(), "매칭 유저가 탐지되었습니다. 헤더의 path를 endpoint로 연결 정보를 전송합니다.", header);
    }

    public void SendStopMatchResult(String endpoint) {
        simpMessageSendingOperations.convertAndSend("/match/"+endpoint, "매칭 취소가 완료되었습니다!");
    }
}