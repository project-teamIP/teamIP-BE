package com.teamip.heyhello.domain.match.collection;

import com.teamip.heyhello.domain.match.dto.RoomStatusDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@Getter
public class ActivateRoom {

    private final List<RoomStatusDto> activateRooms = new CopyOnWriteArrayList<>();

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분(ms 단위)
    public void removeInactiveRooms() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        List<RoomStatusDto> removedRooms = new ArrayList<>();
        Iterator<RoomStatusDto> iterator = activateRooms.iterator();

        while (iterator.hasNext()) {
            RoomStatusDto roomStatusDto = iterator.next();
            if (roomStatusDto.getCreatedAt().isBefore(thirtyMinutesAgo)) {
                iterator.remove();
                removedRooms.add(roomStatusDto);
            }
        }

        log.info("Removed {} inactive rooms.", removedRooms.size());
    }
}
