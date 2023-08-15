package com.teamip.heyhello.domain.socketio.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
import com.teamip.heyhello.domain.socketio.dto.MatchUserInfoDto;
import com.teamip.heyhello.domain.socketio.dto.RequestUserDto;
import com.teamip.heyhello.domain.socketio.dto.WaitUserDto;
import com.teamip.heyhello.domain.socketio.entity.UserList;
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
    private final UserList userList;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public synchronized void findMatch(SocketIOServer server, SocketIOClient client, RequestUserDto requestUserDto) {
        User user = userRepository.findById(requestUserDto.getUserId()).orElseThrow(() -> new NullPointerException("없는 유저입니다."));
       log.info("현재 해당 서버 세션 수 = {}", server.getAllClients().size());
        if (userList.getLists().isEmpty()) {
            addUserToQueueIfEmpty(client, user);
            return;
        }
        isUserAlreadyInCollection(client);

        searchUserFromCondition(server, client, user);
    }

    private void isUserAlreadyInCollection(SocketIOClient client) {
        if (userList.getLists().containsKey(client.getSessionId())) {
            client.sendEvent("error", "이미 대기열에 등록된 유저입니다.");
        }
    }

    private void addUserToQueueIfEmpty(SocketIOClient client, User user) {
        userList.getLists().put(client.getSessionId(),
                WaitUserDto.builder()
                        .user(user)
                        .sessionId(client.getSessionId())
                        .build());
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
    }

    private void searchUserFromCondition(SocketIOServer server, SocketIOClient client, User requestUser) {

        for (WaitUserDto waitUserDto : userList.getLists().values()) {
            if (isNotSameLanguage(waitUserDto.getUser(), requestUser) && isNotBlockedEach(waitUserDto.getUser(), requestUser)) {
                userList.getLists().remove(waitUserDto.getSessionId());
                UUID uuid = UUID.randomUUID();
                SocketIOClient waitClient = server.getClient(waitUserDto.getSessionId());
                waitClient.joinRoom(uuid.toString());
                client.joinRoom(uuid.toString());

                client.sendEvent("success", MatchUserInfoDto.builder()
                        .nickname(waitUserDto.getUser().getNickname())
                        .country(waitUserDto.getUser().getCountry())
                        .roomId(uuid)
                        .build());
                return;
            }
        }
        userList.getLists().put(client.getSessionId(),
                WaitUserDto.builder()
                        .user(requestUser)
                        .sessionId(client.getSessionId())
                        .build());
        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
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
