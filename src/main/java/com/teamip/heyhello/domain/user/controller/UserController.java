package com.teamip.heyhello.domain.user.controller;

import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(signupRequestDto));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<StatusResponseDto> withdrawal(@RequestParam Long userId){

        return ResponseEntity.ok(userService.withdrawal(userId));
    }
}
