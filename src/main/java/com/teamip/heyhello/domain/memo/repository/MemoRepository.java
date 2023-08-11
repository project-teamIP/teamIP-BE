package com.teamip.heyhello.domain.memo.repository;

import com.teamip.heyhello.domain.memo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo,Long> {
    List<Memo> findByUserId(Long userId);

    Page<Memo> findByUserId(Long userId, Pageable pageable);
}
