package com.teamip.heyhello.domain.match.controller;

import com.teamip.heyhello.domain.match.dto.RoomStatusDto;
import com.teamip.heyhello.domain.match.dto.MatchInfoRequestDto;
import com.teamip.heyhello.domain.match.service.MatchService;
import com.teamip.heyhello.domain.match.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;
    private final MessageService messageService;

    @MessageMapping("/{endpoint}")
    public void checkMatch(@DestinationVariable String endpoint, Message message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        MatchInfoRequestDto matchInfoRequestDto = messageService.parseHeadersInfo(headerAccessor);
        matchService.findMatch(matchInfoRequestDto);
        RoomStatusDto roomStatusDto = null;
        if (matchInfoRequestDto.isMatched()) {
            roomStatusDto = matchService.checkDirectMatch(matchInfoRequestDto.getUser().getLoginId());
        }
        messageService.SendSearchResult(endpoint, roomStatusDto);
    }
}
