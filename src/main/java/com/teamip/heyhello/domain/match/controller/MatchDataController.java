package com.teamip.heyhello.domain.match.controller;

import com.teamip.heyhello.domain.match.dto.MatchRoomResponseDto;
import com.teamip.heyhello.domain.match.service.MatchDataService;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MatchDataController {

    private final MatchDataService matchDataService;

    @GetMapping("/recent-call")
    public ResponseEntity<Page<MatchRoomResponseDto>> getUsersRecentCall(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @PageableDefault(page=1, size = 6, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(matchDataService.getUsersRecentCall(userDetails, pageable));
    }
}
