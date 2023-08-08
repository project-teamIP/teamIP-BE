package com.teamip.heyhello.domain.match.service;

import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
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
    private final BlockRepository blockRepository;

    @Autowired
    public MatchService(ChatQueue chatQueue, ActivateRoom activeRoom, UserRepository userRepository, BlockRepository blockRepository) {
        this.chatQueue = chatQueue;
        this.activeRoom = activeRoom;
        this.userRepository = userRepository;
        this.blockRepository = blockRepository;
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
        if (chatQueue.getChatQueue().isEmpty()) {
            addUserToQueueIfEmpty(matchInfoRequestDto);
            return;
        }
        isUserAlreadyInCollection(matchInfoRequestDto);

        searchUserFromCondition(matchInfoRequestDto);
    }

    private void addUserToQueueIfEmpty(MatchInfoRequestDto matchInfoRequestDto) {
        chatQueue.getChatQueue().put(matchInfoRequestDto.getUser().getId(), matchInfoRequestDto);
    }

    private void isUserAlreadyInCollection(MatchInfoRequestDto matchInfoRequestDto) {
        if (chatQueue.getChatQueue().containsKey(matchInfoRequestDto.getUser().getId())) {
            throw new IllegalArgumentException("이미 대기열에 등록된 유저입니다. 매칭 정보를 변경하고 싶은 경우 매칭 취소 후 다시 시도해주세요");
        }
    }

    private void searchUserFromCondition(MatchInfoRequestDto requestUserDto) {
        for (MatchInfoRequestDto waitUserDto : chatQueue.getChatQueue().values()) {
            if (isTargetLanguage(waitUserDto, requestUserDto) && isNotBlockedEach(waitUserDto, requestUserDto)) {
                    activeRoom.getActivateRooms().add(RoomStatusDto.of(waitUserDto, requestUserDto));
                chatQueue.getChatQueue().remove(waitUserDto.getUser().getId());
                log.info("현재 활성화된 대화 방 수 : " + activeRoom.getActivateRooms().size());
                requestUserDto.successMatch(waitUserDto);
                return;
            }
        }
        chatQueue.getChatQueue().put(requestUserDto.getUser().getId(), requestUserDto);
    }

    private boolean isTargetLanguage(MatchInfoRequestDto waitUserDto, MatchInfoRequestDto requestUserDto) {
        return waitUserDto.getUserLanguage().equals(requestUserDto.getTargetLanguage());
    }
    private boolean isNotBlockedEach(MatchInfoRequestDto waitUserDto, MatchInfoRequestDto requestUserDto) {
        Block block1 = blockRepository.findByRequestUserAndTargetUser(requestUserDto.getUser(), waitUserDto.getUser()).orElse(null);
        Block block2 = blockRepository.findByRequestUserAndTargetUser(waitUserDto.getUser(), requestUserDto.getUser()).orElse(null);
        if (block1 != null || block2 != null) {

            return false;
        }
        return true;

    }
    public void removeFromList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("유저 정보가 잘못되었습니다. 요청을 다시 확인해주세요."));
        log.info( "chatQueue size = "+chatQueue.getChatQueue().size());
        chatQueue.getChatQueue().remove(userId);
        log.info("remove 후 chatQueue size = " + chatQueue.getChatQueue().size());
    }
}
