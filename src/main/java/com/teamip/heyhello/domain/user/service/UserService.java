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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void initializeUserInfo(Long userId, String country, String gender, String language, String interest) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // 회원 정보 업데이트
            user.initializeUserInfo(country, gender,language, interest);
        }
    }


    public StatusResponseDto withdrawal(Long userId) {

        return new StatusResponseDto(HttpStatus.OK, "회원 탈퇴 성공");
    }


    private void checkDuplicatedValue(User user) {
        isExistedloginId(user.getLoginId());
        isExistedNickname(user.getNickname());
    }

    private void isExistedNickname(String nickname) {
        User findUser = userRepository.findByNickname(nickname).orElse(null);
        if (findUser != null) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

    }

    private void isExistedloginId(String loginId) {
        User findUser = userRepository.findByLoginId(loginId).orElse(null);
        if (findUser != null) {
            throw new RuntimeException("이미 존재하는 계정입니다.");
        }
    }


}
