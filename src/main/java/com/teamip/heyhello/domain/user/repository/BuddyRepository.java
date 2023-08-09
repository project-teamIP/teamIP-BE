package com.teamip.heyhello.domain.user.repository;

import com.teamip.heyhello.domain.user.entity.Buddy;
import com.teamip.heyhello.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
    List<Buddy> findBySender(User sender);

    Page<Buddy> findBySender(User sender, Pageable pageable);

    Optional<Buddy> findBySenderAndReceiver(User sender, User Receiver);

    void deleteBySenderAndReceiver(User sender, User receiver);
}
