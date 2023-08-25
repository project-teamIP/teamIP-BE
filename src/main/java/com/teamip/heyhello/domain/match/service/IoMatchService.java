package com.teamip.heyhello.domain.match.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
import com.teamip.heyhello.domain.match.dto.MatchUserInfoDto;
import com.teamip.heyhello.domain.match.dto.RequestUserDto;
import com.teamip.heyhello.domain.match.dto.WaitUserDto;
import com.teamip.heyhello.domain.match.entity.MatchRoom;
import com.teamip.heyhello.domain.match.repository.RoomRepository;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.redis.WaitUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoMatchService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;
    private final WaitUserRepository waitUserRepository;

    public synchronized void findMatch(SocketIOServer server, SocketIOClient client, RequestUserDto requestUserDto) {
        User user = userRepository.findByLoginId(requestUserDto.getLoginId()).orElseThrow(() -> new NullPointerException("없는 유저입니다."));
        if (waitUserRepository.isWaitUserListEmpty()) {
            addUserToSet(client, user);
            return;
        }
        if (isAlreadyExistInCollection(client, user)) {
            return;
        }
        ;
        searchUserFromCondition(server, client, user);
    }

    private boolean isAlreadyExistInCollection(SocketIOClient client, User user) {
        if (isUserAlreadyExistInWaitList(user)) {
            client.sendEvent("error", "이미 대기열에 등록된 사용자입니다.");
            return true;
        }
        return false;
    }

    private boolean isUserAlreadyExistInWaitList(User user) {
        for (ZSetOperations.TypedTuple<WaitUserDto> tuple : waitUserRepository.getWaitUserList()) {
            WaitUserDto waitUserDto = tuple.getValue();
            if (waitUserDto.getUserId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
    private boolean isUserAlreadyExistInWaitList(SocketIOClient client) {
        for (ZSetOperations.TypedTuple<WaitUserDto> tuple : waitUserRepository.getWaitUserList()) {
            WaitUserDto waitUserDto = tuple.getValue();
            if (waitUserDto.getSessionId().equals(client.getSessionId())) {
                return true;
            }
        }
        return false;
    }

    private void addUserToSet(SocketIOClient client, User user) {
        waitUserRepository.addWaitUser(WaitUserDto.builder()
                .user(user)
                .sessionId(client.getSessionId())
                .build());
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
    }

    public void cancelFindMatch(SocketIOServer server, SocketIOClient client, String message) {
        if (isUserAlreadyExistInWaitList(client)) {
            waitUserRepository.removeWaitUserAtList(client);
            client.sendEvent(SocketProperty.CANCEL_KEY, "매칭 취소가 완료되었습니다.");
        } else {
            client.sendEvent(SocketProperty.ERROR_KEY, "현재 매칭 중이 아닙니다.");
        }

    }

    private void searchUserFromCondition(SocketIOServer server, SocketIOClient client, User requestUser) {
        for (ZSetOperations.TypedTuple<WaitUserDto> tuple : waitUserRepository.getWaitUserList()) {
            WaitUserDto waitUserDto = tuple.getValue();
            if (isNotSameLanguage(waitUserDto.getLanguage(), requestUser.getLanguage()) && isNotBlockedEach(waitUserDto.getUserId(), requestUser.getId())) {
                UUID uuid = createRoomAndEnteredEachUser(server, client, requestUser, waitUserDto);
                client.sendEvent("success", "매치완료. offer를 발송합니다.");
                return;
            }
        }
        addUserToSet(client, requestUser);
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
    }

    private UUID createRoomAndEnteredEachUser(SocketIOServer server, SocketIOClient client, User requestUser, WaitUserDto waitUserDto) {
        waitUserRepository.removeWaitUserAtList(waitUserDto);
        UUID uuid = UUID.randomUUID();
        SocketIOClient waitClient = server.getClient(waitUserDto.getSessionId());
        waitClient.joinRoom(uuid.toString());
        client.joinRoom(uuid.toString());
        MatchRoom matchRoom = MatchRoom.builder()
                .user1(userRepository.findById(waitUserDto.getUserId()).orElseThrow(()-> new NullPointerException("해당 유저를 찾을 수 없습니다.")))
                .user1Client(waitClient.getSessionId())
                .user2(requestUser)
                .user2Client(client.getSessionId())
                .roomName(uuid)
                .build();
        roomRepository.save(matchRoom);
        return uuid;
    }

    private boolean isNotSameLanguage(String waitUserLanguage, String requestUserLanguage) {
        return !requestUserLanguage.equals(waitUserLanguage);
    }

    private boolean isNotBlockedEach(Long waitUserId, Long requestUserId) {
        Block block1 = blockRepository.findByRequestUserIdAndTargetUserId(requestUserId, waitUserId).orElse(null);
        Block block2 = blockRepository.findByRequestUserIdAndTargetUserId(waitUserId, requestUserId).orElse(null);
        if (block1 != null || block2 != null) {
            return false;
        }
        return true;
    }

    @Transactional
    public void endCall(SocketIOServer server, SocketIOClient client) {
        MatchRoom matchRoom = getMatchRoomFromClientInfo(client);
        if(matchRoom==null){
            client.sendEvent(SocketProperty.ERROR_KEY, "참여 중인 방이 없습니다.");
        }
        if (matchRoom.isActive()) {
            matchRoom.updateIsActiveToFalse();
            server.getRoomOperations(matchRoom.getRoomName().toString()).sendEvent(SocketProperty.ENDCALL_KEY, "대화가 종료되었어요!");
            server.getClient(matchRoom.getUser1Client()).disconnect();
            server.getClient(matchRoom.getUser2Client()).disconnect();
        } else {
            client.sendEvent(SocketProperty.ERROR_KEY, "이미 종료된 대화방입니다.");
        }
    }

    public MatchRoom getMatchRoomFromClientInfo(SocketIOClient client) {
        return roomRepository.findByRoomName(
                        UUID.fromString(client.getAllRooms()
                                .stream()
                                .toList()
                                .get(1)))
                .orElse(null);
    }

    public void sendMatchUserInfo(SocketIOServer server, SocketIOClient client, MatchUserInfoDto message) throws JsonProcessingException {
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.MATCH_USER_INFO_KEY, client, message);
    }
    public void sendAnswerUserInfo(SocketIOServer server, SocketIOClient client, MatchUserInfoDto message) throws JsonProcessingException {
        server.getRoomOperations(client.getAllRooms().stream().toList().get(1)).sendEvent(SocketProperty.ANSWER_USER_INFO_KEY, client, message);
    }
}
