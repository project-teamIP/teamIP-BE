package com.teamip.heyhello.domain.hourtraffic.service;

import com.teamip.heyhello.domain.hourtraffic.entity.HourTraffic;
import com.teamip.heyhello.domain.hourtraffic.repository.HourTrafficRepository;
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HourTrafficService {

    private final HourTrafficRepository hourTrafficRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // @Scheduled(cron = "0 0 * * * *")
    // public void saveHourTraffic() {
    //     // HourTraffic 엔티티를 생성하고 저장하는 로직
    //     HourTraffic hourTraffic = HourTraffic.builder()
    //             .userCount(refreshTokenRepository.countRefreshTokens())
    //             .build();

    //     hourTrafficRepository.save(hourTraffic);
    // }

    public List<Integer> todayUserCountListByHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);

        List<HourTraffic> hourlyTrafficList = hourTrafficRepository.findByTimeBetween(startOfDay, endOfDay);
        return hourlyTrafficList.stream().map(HourTraffic::getUserCount).toList();
    }

}
