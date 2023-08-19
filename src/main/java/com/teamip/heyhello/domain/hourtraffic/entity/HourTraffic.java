package com.teamip.heyhello.domain.hourtraffic.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EnableJpaAuditing
public class HourTraffic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer userCount;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time;

    @Builder
    public HourTraffic(Integer userCount) {
        this.userCount = userCount;
        this.time = LocalDateTime.now();
    }
}
