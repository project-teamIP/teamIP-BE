package com.teamip.heyhello.domain.user.service;

import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.StatusResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StatusResponseDto signup(SignupRequestDto signupRequestDto) {

        User user = User.of(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()));
        checkDuplicatedValue(user);
        userRepository.save(user);

        return StatusResponseDto.builder()
                .message("가입이 완료되었습니다.")
                .status(HttpStatus.CREATED)
                .build();
    }

    private void checkDuplicatedValue(User user) {
        isExistedEmail(user.getUsername());
        isExistedNickname(user.getNickname());
    }

    private void isExistedNickname(String nickname) {
        User findUser = userRepository.findByNickname(nickname).orElse(null);
        if (findUser != null) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

    }

    private void isExistedEmail(String email) {
        User findUser = userRepository.findByUsername(email).orElse(null);
        if (findUser != null) {
            throw new RuntimeException("이미 존재하는 계정입니다.");
        }
    }

    public StatusResponseDto withdrawal(Long userId) {

        return new StatusResponseDto(HttpStatus.OK, "회원 탈퇴 성공");
    }
}
