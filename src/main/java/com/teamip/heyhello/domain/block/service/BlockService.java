package com.teamip.heyhello.domain.block.service;

import com.teamip.heyhello.domain.block.entity.Block;
import com.teamip.heyhello.domain.block.repository.BlockRepository;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public StatusResponseDto blockUser(UserDetailsImpl userDetails, String nickname) {

        User requestUser = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        User targetUser = userRepository.findByNickname(nickname).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        checkRequestValidity(requestUser, targetUser);

        Block block = blockRepository.findByRequestUserAndTargetUser(requestUser, targetUser).orElse(null);
        if (block != null) {
            blockRepository.delete(block);
            return StatusResponseDto.builder()
                    .status(HttpStatus.OK)
                    .message("차단이 해제되었습니다.")
                    .build();
        }
        block = Block.builder()
                .requestUser(requestUser)
                .targetUser(targetUser)
                .build();
        blockRepository.save(block);
        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message("차단이 완료되었습니다.")
                .build();
    }

    private void checkRequestValidity(User requestUser, User targetUser) {

        if (requestUser.getNickname().equals(targetUser.getNickname())) {
            throw new RuntimeException("자기 자신을 차단할 수 없습니다.");
        }
    }
}
