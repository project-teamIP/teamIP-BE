package com.teamip.heyhello.domain.hourtraffic.repository;

import com.teamip.heyhello.domain.hourtraffic.entity.HourTraffic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HourTrafficRepository extends JpaRepository<HourTraffic, Long> {
    List<HourTraffic> findByTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
