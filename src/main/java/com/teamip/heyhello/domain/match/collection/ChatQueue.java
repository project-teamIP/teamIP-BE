package com.teamip.heyhello.domain.match.collection;

import com.teamip.heyhello.domain.match.dto.MatchInfoRequestDto;
import com.teamip.heyhello.domain.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
@Getter
public class ChatQueue {

    private final Map<Long, MatchInfoRequestDto> chatQueue = Collections.synchronizedMap(new LinkedHashMap<>());

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분(ms 단위)
    public void removeInactiveUserMatches() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        List<String> removedUserIds = new ArrayList<>();
        synchronized (chatQueue) {
            Iterator<Map.Entry<Long, MatchInfoRequestDto>> iterator = chatQueue.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Long, MatchInfoRequestDto> entry = iterator.next();
                MatchInfoRequestDto matchInfoRequestDto = entry.getValue();
                if (matchInfoRequestDto.getCreatedAt().isBefore(thirtyMinutesAgo)) {
                    iterator.remove();
                    removedUserIds.add(entry.getKey().toString());
                }
            }
        }

        log.info("Removed {} inactive MatchInfoRequestDto(s) with userId(s): {}", removedUserIds.size(), removedUserIds);
    }
}
