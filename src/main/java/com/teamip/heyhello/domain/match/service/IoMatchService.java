package com.teamip.heyhello.domain.match.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
import com.teamip.heyhello.domain.match.dto.RequestUserDto;
import com.teamip.heyhello.domain.match.dto.WaitUserDto;
import com.teamip.heyhello.domain.match.entity.MatchRoom;
import com.teamip.heyhello.domain.match.entity.MatchRoomList;
import com.teamip.heyhello.domain.match.entity.MatchUserList;
import com.teamip.heyhello.domain.match.repository.RoomRepository;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoMatchService {
    private final MatchUserList matchUserList;
    private final MatchRoomList matchRoomList;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final RoomRepository roomRepository;

    public synchronized void findMatch(SocketIOServer server, SocketIOClient client, RequestUserDto requestUserDto) {
        User user = userRepository.findById(requestUserDto.getUserId()).orElseThrow(() -> new NullPointerException("없는 유저입니다."));
        log.info("현재 해당 서버 세션 수 = {}", server.getAllClients().size());
        if (matchUserList.getLists().isEmpty()) {
            addUserToQueueIfEmpty(client, user);
            return;
        }
        if (isUserAlreadyInCollection(client, user)) {
            return;
        }
        ;
        searchUserFromCondition(server, client, user);
    }

    private boolean isUserAlreadyInCollection(SocketIOClient client, User user) {
        if (isUserOrSessionInList(client, user)) {
            client.sendEvent("error", "이미 대기열에 등록된 사용자입니다.");
            return true;
        }
        return false;
    }

    private boolean isUserOrSessionInList(SocketIOClient client, User user) {
        return matchUserList.getLists().containsKey(client.getSessionId());
    }

    private void addUserToQueueIfEmpty(SocketIOClient client, User user) {
        matchUserList.getLists().put(client.getSessionId(),
                WaitUserDto.builder()
                        .user(user)
                        .sessionId(client.getSessionId())
                        .build());
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
    }
    public void cancelFindMatch(SocketIOServer server, SocketIOClient client, String message) {
        if (matchUserList.getLists().containsKey(client.getSessionId())) {
            matchUserList.getLists().remove(client.getSessionId());
            client.sendEvent(SocketProperty.CANCEL_KEY, "매칭 취소가 완료되었습니다.");
        } else {
            client.sendEvent(SocketProperty.ERROR_KEY, "현재 매칭 중이 아닙니다.");
        }

    }

    private void searchUserFromCondition(SocketIOServer server, SocketIOClient client, User requestUser) {

        for (WaitUserDto waitUserDto : matchUserList.getLists().values()) {
            if (isNotSameLanguage(waitUserDto.getUser(), requestUser) && isNotBlockedEach(waitUserDto.getUser(), requestUser)) {
                UUID uuid = createRoomAndEnteredEachUser(server, client, requestUser, waitUserDto);
                client.sendEvent("success", "매치완료. offer를 발송합니다.");
                return;
            }
        }
        matchUserList.getLists().put(client.getSessionId(),
                WaitUserDto.builder()
                        .user(requestUser)
                        .sessionId(client.getSessionId())
                        .build());
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
    }

    private UUID createRoomAndEnteredEachUser(SocketIOServer server, SocketIOClient client, User requestUser, WaitUserDto waitUserDto) {
        matchUserList.getLists().remove(waitUserDto.getSessionId());
        UUID uuid = UUID.randomUUID();
        SocketIOClient waitClient = server.getClient(waitUserDto.getSessionId());
        waitClient.joinRoom(uuid.toString());
        client.joinRoom(uuid.toString());
        MatchRoom matchRoom = MatchRoom.builder().user1(waitUserDto.getUser()).user2(requestUser).roomName(uuid).build();
        roomRepository.save(matchRoom);
        matchRoomList.getLists().put(uuid, matchRoom);
        return uuid;
    }

    private boolean isNotSameLanguage(User waitUser, User requestUser) {
        return !waitUser.getLanguage().equals(requestUser.getLanguage());
    }

    private boolean isNotBlockedEach(User waitUser, User requestUser) {
        Block block1 = blockRepository.findByRequestUserAndTargetUser(requestUser, waitUser).orElse(null);
        Block block2 = blockRepository.findByRequestUserAndTargetUser(waitUser, requestUser).orElse(null);
        if (block1 != null || block2 != null) {
            return false;
        }
        return true;
    }

    @Transactional
    public void endCall(SocketIOServer server, SocketIOClient client, String message) {
        MatchRoom matchRoom = getMatchRoomFromClientInfo(client);
        if (matchRoom.isActive()) {
            matchRoom.updateIsActiveToFalse();
            server.getRoomOperations(matchRoom.getRoomName().toString()).sendEvent(SocketProperty.ENDCALL_KEY, "대화가 종료되었어요!");
        } else {
            client.sendEvent(SocketProperty.ENDCALL_KEY, "이미 종료된 대화방입니다.");
        }
        matchRoomList.getLists().remove(matchRoom.getRoomName());
    }

    private MatchRoom getMatchRoomFromClientInfo(SocketIOClient client) {
        MatchRoom matchRoom = roomRepository.findByRoomName(
                        UUID.fromString(client.getAllRooms()
                                .stream()
                                .toList()
                                .get(1)))
                .orElseThrow(() -> new NullPointerException("해당 방 정보를 찾을 수 없습니다"));
        return matchRoom;
    }
}
