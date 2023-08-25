//package com.teamip.heyhello.global.test.service;
//
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.teamip.heyhello.domain.block.entity.Block;
//import com.teamip.heyhello.domain.block.repository.BlockRepository;
//import com.teamip.heyhello.domain.match.dto.RequestUserDto;
//import com.teamip.heyhello.domain.match.entity.MatchRoom;
//import com.teamip.heyhello.domain.match.repository.RoomRepository;
//import com.teamip.heyhello.domain.user.entity.User;
//import com.teamip.heyhello.domain.user.repository.UserRepository;
//import com.teamip.heyhello.global.test.entity.WaitList;
//import com.teamip.heyhello.global.test.repository.WaitListRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class WaitListService {
//
//    ///여기부턴 RDB 매치(부하 테스트용)
//    private final WaitListRepository waitListRepository;
//    private final UserRepository userRepository;
//    private final BlockRepository blockRepository;
//    private final RoomRepository roomRepository;
//
//    public synchronized void findMatchDb(SocketIOServer server, SocketIOClient client, RequestUserDto requestUserDto) {
//        User user = userRepository.findByLoginId(requestUserDto.getLoginId()).orElseThrow(() -> new NullPointerException("없는 유저입니다."));
//        List<WaitList> waitLists = waitListRepository.findAll();
//
//        if (waitLists.isEmpty()) {
//            addUserToList(client, user);
//            return;
//        } else{
//            log.info("현재 대기자 수 = {}", waitLists.size());
//        }
//        if (isAlreadyExistInDB(client, user, waitLists)) {
//            return;
//        }
//        searchUserFromConditionDB(server, client, user, waitLists);
//    }
//
//    private void addUserToList(SocketIOClient client, User user) {
//        UUID sessionId = client.getSessionId();
//        waitListRepository.save(WaitList.builder()
//                .userId(user.getId())
//                .nickname(user.getNickname())
//                .language(user.getLanguage())
//                .sessionId(sessionId).build());
//
//        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
//    }
//    private boolean isAlreadyExistInDB(SocketIOClient client, User user, List<WaitList> waitLists) {
//        for(WaitList waitList: waitLists){
//            if(waitList.getUserId().equals(user.getId())){
//                client.sendEvent("error", "이미 대기열에 등록된 사용자입니다.");
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private synchronized void searchUserFromConditionDB(SocketIOServer server, SocketIOClient client, User requestUser, List<WaitList> waitLists) {
//        for (WaitList waitList: waitLists) {
//            if (isNotSameLanguage(waitList.getLanguage(), requestUser.getLanguage()) && isNotBlockedEach(waitList.getUserId(), requestUser.getId())) {
//                UUID uuid = createRoomAndEnteredEachUserDB(server, client, requestUser, waitList);
//                client.sendEvent("success", "매치완료. offer를 발송합니다.");
//                return;
//            }
//        }
//        addUserToList(client, requestUser);
//        client.sendEvent("wait", "적절한 매칭 상대가 없습니다. 조금 더 기다려주세요.");
//    }
//    private boolean isNotSameLanguage(String waitUserLanguage, String requestUserLanguage) {
//        return !requestUserLanguage.equals(waitUserLanguage);
//    }
//    private boolean isNotBlockedEach(Long waitUserId, Long requestUserId) {
//        Block block1 = blockRepository.findByRequestUserIdAndTargetUserId(requestUserId, waitUserId).orElse(null);
//        Block block2 = blockRepository.findByRequestUserIdAndTargetUserId(waitUserId, requestUserId).orElse(null);
//        if (block1 != null || block2 != null) {
//            return false;
//        }
//        return true;
//    }
//    @Transactional
//    public synchronized UUID createRoomAndEnteredEachUserDB(SocketIOServer server, SocketIOClient client, User requestUser, WaitList waitList) {
//        waitListRepository.delete(waitList);
//        UUID uuid = UUID.randomUUID();
//        SocketIOClient waitClient = server.getClient(waitList.getSessionId());
//        waitClient.joinRoom(uuid.toString());
//        client.joinRoom(uuid.toString());
//        MatchRoom matchRoom = MatchRoom.builder()
//                .user1(userRepository.findById(waitList.getUserId()).orElseThrow(() -> new NullPointerException("해당 유저를 찾을 수 없습니다.")))
//                .user1Client(waitClient.getSessionId())
//                .user2(requestUser)
//                .user2Client(client.getSessionId())
//                .roomName(uuid)
//                .build();
//        roomRepository.save(matchRoom);
//        return uuid;
//    }
//}
