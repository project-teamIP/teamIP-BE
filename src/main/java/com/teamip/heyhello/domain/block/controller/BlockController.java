package com.teamip.heyhello.domain.block.controller;

import com.teamip.heyhello.domain.block.service.BlockService;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class BlockController {
    private final BlockService blockService;

    @PostMapping("/block/{nickname}")
    public ResponseEntity<StatusResponseDto> block(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable String nickname) {
        return ResponseEntity.ok(blockService.blockUser(userDetails, nickname));
    }
}
