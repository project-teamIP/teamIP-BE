package com.teamip.heyhello.global.redis;

import com.corundumstudio.socketio.SocketIOClient;
import com.teamip.heyhello.domain.match.dto.WaitUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Slf4j
public class WaitUserRepository {

    private static final String WAIT_USER_LIST = "waitUserList";
    private final RedisTemplate<String, WaitUserDto> matchRedisTemplate;

    @Autowired
    public WaitUserRepository(@Qualifier("matchRedisTemplate") RedisTemplate<String, WaitUserDto> matchRedisTemplate) {
        this.matchRedisTemplate = matchRedisTemplate;
    }

    public void addWaitUser(WaitUserDto waitUserDto) {
        Double presentIndex = getHighestIndex();
        try {
            matchRedisTemplate.opsForZSet().add(WAIT_USER_LIST, waitUserDto, presentIndex+1);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Double getHighestIndex() {
        double presentIndex;
        Set<ZSetOperations.TypedTuple<WaitUserDto>> result = matchRedisTemplate.opsForZSet().reverseRangeWithScores(WAIT_USER_LIST, 0, 0);
        if (!result.isEmpty()) {
            ZSetOperations.TypedTuple<WaitUserDto> highestScoreEntry = result.iterator().next();
            presentIndex = highestScoreEntry.getScore();
        } else {
            presentIndex = 0;
        }
        return presentIndex;
    }

    public boolean isWaitUserListEmpty() {
        Long userListSize = matchRedisTemplate.opsForZSet().size(WAIT_USER_LIST);
        if (userListSize == null || userListSize == 0) {
            return true;
        }
        return false;
    }

    public Set<ZSetOperations.TypedTuple<WaitUserDto>> getWaitUserList() {
        Double minScore = Double.NEGATIVE_INFINITY;
        Double maxScore = Double.MAX_VALUE;
        return matchRedisTemplate.opsForZSet().rangeByScoreWithScores(WAIT_USER_LIST, minScore, maxScore);
    }


    public void removeWaitUserAtList(WaitUserDto waitUserDto) {
        for (ZSetOperations.TypedTuple<WaitUserDto> userTuple : getWaitUserList()) {
            WaitUserDto userDto = userTuple.getValue();

            if (userDto.getSessionId().equals(waitUserDto.getSessionId())) {
                matchRedisTemplate.opsForZSet().remove(WAIT_USER_LIST, waitUserDto);

            }
        }
    }
    public void removeWaitUserAtList(SocketIOClient client) {
        for (ZSetOperations.TypedTuple<WaitUserDto> userTuple : getWaitUserList()) {
            WaitUserDto userDto = userTuple.getValue();

            if (userDto.getSessionId().equals(client.getSessionId())) {
                matchRedisTemplate.opsForZSet().remove(WAIT_USER_LIST, userDto);
            }
        }
    }
}
