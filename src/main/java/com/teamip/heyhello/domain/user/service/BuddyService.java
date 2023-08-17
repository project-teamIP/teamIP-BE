package com.teamip.heyhello.domain.user.service;

import com.teamip.heyhello.domain.user.dto.BuddyResponseDto;
import com.teamip.heyhello.domain.user.entity.Buddy;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.BuddyRepository;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuddyService {
    private final BuddyRepository buddyRepository;
    private final UserRepository userRepository;

    public StatusResponseDto addBuddy(UserDetailsImpl userDetails, String nickname) {
        User sender = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        User receiver = userRepository.findByNickname(nickname).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        Optional<Buddy> optionalBuddy = buddyRepository.findBySenderAndReceiver(sender, receiver);
        if (optionalBuddy.isPresent()) {
            throw new RuntimeException("마 이미 깐부아이가!");
        }

        Buddy buddy = Buddy.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        buddyRepository.save(buddy);

        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message("친구추가가 완료되었습니다.")
                .build();
    }

    public Page<BuddyResponseDto> getBuddies(UserDetailsImpl userDetails, Pageable pageable) {
        User sender = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        int currentPage = pageable.getPageNumber();
        int newPage = currentPage <= 1 ? 0 : currentPage - 1;

        Pageable newPageable = PageRequest.of(newPage, pageable.getPageSize(), pageable.getSort());
        Page<Buddy> buddyPage = buddyRepository.findBySender(sender, newPageable);
        List<BuddyResponseDto> buddyResponseDtos = buddyPage.getContent().stream().map(
                buddy -> BuddyResponseDto.builder()
                        .image(buddy.getReceiver().getImage())
                        .loginId(buddy.getReceiver().getLoginId())
                        .nickname(buddy.getReceiver().getNickname())
                        .build()
        ).toList();

        return new PageImpl<>(buddyResponseDtos, newPageable, buddyPage.getTotalElements());
    }

    @Transactional
    public StatusResponseDto deleteBuddy(UserDetailsImpl userDetails, String nickname) {
        User sender = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        User receiver = userRepository.findByNickname(nickname).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        buddyRepository.deleteBySenderAndReceiver(sender, receiver);

        return StatusResponseDto.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("친구삭제 성공")
                .build();
    }
}
