package com.teamip.heyhello.domain.user.repository;

import com.teamip.heyhello.domain.user.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserlogRepository extends JpaRepository<UserLog, Long> {
    Optional<UserLog> findByUserId(Long userId);
}
