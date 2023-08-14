package com.teamip.heyhello.domain.user.service;

import com.teamip.heyhello.domain.user.dto.MypageResponseDto;
import com.teamip.heyhello.domain.user.dto.SignupRequestDto;
import com.teamip.heyhello.domain.user.dto.UpdateProfileDto;
import com.teamip.heyhello.domain.user.dto.UrlResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    public StatusResponseDto signup(SignupRequestDto signupRequestDto) {
        String defaultUrl = setRandomDefaultImageUrl();
        User user = User.of(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), defaultUrl);
        checkDuplicatedValue(user);
        userRepository.save(user);

        return StatusResponseDto.builder()
                .message("가입이 완료되었습니다.")
                .status(HttpStatus.CREATED)
                .build();
    }

    public StatusResponseDto checkDuplicated(String email, String nickname) {
        if (email == null && nickname == null) {
            return StatusResponseDto.builder().
                    status(HttpStatus.BAD_REQUEST)
                    .message("필요한 필드가 입력되지 않았습니다.")
                    .build();
        }
        if (email == null) {
            return checkNicknameDuplicated(nickname);
        }
        if (nickname == null) {
            return checkEmailDuplicated(email);
        }
        return StatusResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("이메일과 닉네임은 동시에 중복확인이 불가능합니다.")
                .build();
    }

    public MypageResponseDto getMypage(UserDetailsImpl userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );

        return MypageResponseDto.of(user);
    }

    @Transactional
    public StatusResponseDto updateProfile(UserDetailsImpl userDetails, UpdateProfileDto updateProfileDto) {
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        user.update(updateProfileDto);
        return StatusResponseDto.builder().status(HttpStatus.OK).message("업데이트 완료").build();
    }


    public StatusResponseDto withdrawal(Long userId) {

        return StatusResponseDto.builder().status(HttpStatus.OK).message("회원 탈퇴 성공").build();
    }


    @Transactional
    public UrlResponseDto modifyProfileImage(UserDetailsImpl userDetails, MultipartFile image) throws IOException {
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        String imageUrl = s3UploadService.saveFile(image);

        if (!user.getImage().contains(".png")) {
            s3UploadService.deleteImage(user.getImage());
        }
        user.modifyProfileImage(imageUrl);

        return UrlResponseDto.builder()
                .url(imageUrl)
                .build();
    }


    public String setRandomDefaultImageUrl() {
        ArrayList<String> defaultImageKey = new ArrayList<>(List.of("profile1.png", "profile2.png", "profile3.png"));

        int randomIndex = random.nextInt(defaultImageKey.size());

        return s3UploadService.s3HostUrl + defaultImageKey.get(randomIndex);
    }

    private StatusResponseDto checkNicknameDuplicated(String nickname) {

        User user = userRepository.findByNickname(nickname).orElse(null);
        if (user == null) {
            return StatusResponseDto.builder().status(HttpStatus.OK).message("사용 가능한 닉네임입니다.").build();
        }
        return StatusResponseDto.builder().status(HttpStatus.OK).message("사용 중인 닉네임입니다.").build();
    }

    private StatusResponseDto checkEmailDuplicated(String email) {
        Optional<User> user = userRepository.findByLoginId(email);
        if (user.isEmpty()) {
            return StatusResponseDto.builder().status(HttpStatus.OK).message("사용 가능한 이메일입니다.").build();
        }
        return StatusResponseDto.builder().status(HttpStatus.OK).message("이미 가입된 이메일입니다.").build();
    }

    private void checkDuplicatedValue(User user) {
        isExistedLoginId(user.getLoginId());
        isExistedNickname(user.getNickname());
    }

    private void isExistedNickname(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(
                user -> {
                    throw new RuntimeException("이미 존재하는 닉네임입니다.");
                }
        );
    }

    private void isExistedLoginId(String loginId) {
        userRepository.findByLoginId(loginId).ifPresent(
                user -> {
                    throw new RuntimeException("이미 존재하는 계정입니다.");
                }
        );
    }


}
