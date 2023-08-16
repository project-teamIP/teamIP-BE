package com.teamip.heyhello.domain.socketio.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
import com.teamip.heyhello.domain.socketio.dto.MatchUserInfoDto;
import com.teamip.heyhello.domain.socketio.dto.RequestUserDto;
import com.teamip.heyhello.domain.socketio.dto.WaitUserDto;
import com.teamip.heyhello.domain.socketio.entity.MatchRoom;
import com.teamip.heyhello.domain.socketio.entity.MatchUserList;
import com.teamip.heyhello.domain.socketio.repository.RoomRepository;
import com.teamip.heyhello.domain.socketio.socket.SocketProperty;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoMatchService {
    private final MatchUserList matchUserList;
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
        if(isUserAlreadyInCollection(client)){
            return;
        };

        searchUserFromCondition(server, client, user);
    }

    private boolean isUserAlreadyInCollection(SocketIOClient client) {
        if (matchUserList.getLists().containsKey(client.getSessionId())) {
            client.sendEvent("error", "이미 대기열에 등록된 사용자입니다.(userId 기준 아님.)");
            return true;
        }
        return false;
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
        if(matchUserList.getLists().containsKey(client.getSessionId())) {
            matchUserList.getLists().remove(client.getSessionId());
            client.sendEvent(SocketProperty.CANCEL_KEY, "매칭 취소가 완료되었습니다.");
        } else{
            client.sendEvent(SocketProperty.ERROR_KEY, "현재 매칭 중이 아닙니다.");
        }

    }
    private void searchUserFromCondition(SocketIOServer server, SocketIOClient client, User requestUser) {

        for (WaitUserDto waitUserDto : matchUserList.getLists().values()) {
            if (isNotSameLanguage(waitUserDto.getUser(), requestUser) && isNotBlockedEach(waitUserDto.getUser(), requestUser)) {
                UUID uuid = createRoomAndEnteredEachUser(server, client, requestUser, waitUserDto);
                client.sendEvent("success", MatchUserInfoDto.builder()
                        .nickname(waitUserDto.getUser().getNickname())
                        .country(waitUserDto.getUser().getCountry())
                        .roomId(uuid)
                        .build());
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


}
