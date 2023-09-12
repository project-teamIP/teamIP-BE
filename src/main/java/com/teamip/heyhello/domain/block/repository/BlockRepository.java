package com.teamip.heyhello.domain.block.repository;

import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByRequestUserIdAndTargetUserId(Long requestUserId, Long targetUserId);
}
