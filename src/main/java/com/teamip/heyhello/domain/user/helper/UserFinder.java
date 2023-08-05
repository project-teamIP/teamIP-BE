package com.teamip.heyhello.domain.user.helper;

import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;

    public User findUserByLoginId(String loginId) {
        Optional<User> user = userRepository.findByLoginId(loginId); 
        return user.orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.") 
        );
    }

    public User findUserByNickname(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
    }

    public List<User> findAll() {

        return userRepository.findAll();
    }
}
