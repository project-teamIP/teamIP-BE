package com.teamip.heyhello.domain.match.service;

import com.teamip.heyhello.domain.match.collection.ActivateRoom;
import com.teamip.heyhello.domain.match.collection.ChatQueue;
import com.teamip.heyhello.domain.match.dto.MatchInfoRequestDto;
import com.teamip.heyhello.domain.match.dto.RoomStatusDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatchService {
    private final ChatQueue chatQueue;
    private final ActivateRoom activeRoom;
    private final UserRepository userRepository;

    @Autowired
    public MatchService(ChatQueue chatQueue, ActivateRoom activeRoom, UserRepository userRepository) {
        this.chatQueue = chatQueue;
        this.activeRoom = activeRoom;
        this.userRepository = userRepository;
    }

    public synchronized RoomStatusDto checkDirectMatch(String loginId) {
        for (RoomStatusDto roomStatusDto : activeRoom.getActivateRooms()) {
            if (roomStatusDto.getUser2().getLoginId().equals(loginId)) {
                return roomStatusDto;
            }
        }
        return null;
    }


    public synchronized void findMatch(MatchInfoRequestDto matchInfoRequestDto) {

        log.info("findMatch 시작");
        if (chatQueue.getChatQueue().isEmpty()) {
            addUserToQueueIfEmpty(matchInfoRequestDto);
            return;
        }
        isUserAlreadyInCollection(matchInfoRequestDto);

        searchUserFromCondition(matchInfoRequestDto);
    }

    private void addUserToQueueIfEmpty(MatchInfoRequestDto matchInfoRequestDto) {
        log.info("대기중인 유저가 없으므로 대기리스트에 추가");
        chatQueue.getChatQueue().put(matchInfoRequestDto.getUser(), matchInfoRequestDto);
    }

    private void isUserAlreadyInCollection(MatchInfoRequestDto matchInfoRequestDto) {
        if (chatQueue.getChatQueue().containsKey(matchInfoRequestDto.getUser())) {
            throw new IllegalArgumentException("이미 대기열에 등록된 유저입니다. 매칭 정보를 변경하고 싶은 경우 매칭 취소 후 다시 시도해주세요");
        }
    }

    private void searchUserFromCondition(MatchInfoRequestDto requestUserDto) {
        for (MatchInfoRequestDto waitUserDto : chatQueue.getChatQueue().values()) {
            if (waitUserDto.getUserLanguage().equals(requestUserDto.getTargetLanguage())) {
                activeRoom.getActivateRooms().add(RoomStatusDto.builder()
                        .user1(waitUserDto.getUser())
                        .user2(requestUserDto.getUser())
                        .user1Endpoint(waitUserDto.getUserEndpoint())
                        .user2Endpoint(requestUserDto.getUserEndpoint())
                        .build()
                );
                chatQueue.getChatQueue().remove(waitUserDto.getUser());
                log.info("매칭 성공. 남은 " + requestUserDto.getTargetLanguage() + " 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");
                log.info("추가 후 현재 활성화된 대화 방 수 : " + activeRoom.getActivateRooms().size());
                requestUserDto.successMatch(waitUserDto);
                return;
            } else {
                chatQueue.getChatQueue().put(requestUserDto.getUser(), requestUserDto);
                log.info("매칭에 실패했으므로 대기 리스트에 추가. 현재 " + requestUserDto.getUserLanguage() + " 사용자 매칭 대기자 : " + chatQueue.getChatQueue().size() + " 명");
                return;
            }
        }
    }

    public void removeFromList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("유저 정보가 잘못되었습니다. 요청을 다시 확인해주세요."));
        chatQueue.getChatQueue().remove(user);
    }
}
