package com.teamip.heyhello.domain.user.service;


import com.amazonaws.services.kms.model.NotFoundException;
import com.teamip.heyhello.domain.hourtraffic.service.HourTrafficService;
import com.teamip.heyhello.domain.match.service.MatchDataService;
import com.teamip.heyhello.domain.memo.service.MemoService;
import com.teamip.heyhello.domain.user.dto.*;
import com.teamip.heyhello.domain.user.entity.*;
import com.teamip.heyhello.domain.user.repository.DashBoardRepository;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.domain.user.repository.UserlogRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import com.teamip.heyhello.global.redis.TokenService;
import com.teamip.heyhello.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MatchDataService matchDataService;
    private final MemoService memoService;
    private final HourTrafficService hourTrafficService;
    private final DashBoardRepository dashBoardRepository;
    private final UserlogRepository userlogRepository;

    public StatusResponseDto signup(SignupRequestDto signupRequestDto) {
        String defaultUrl = setRandomDefaultImageUrl();
        User user = User.of(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), defaultUrl);
        checkDuplicatedValue(user);

        if (signupRequestDto.getInterests() != null) {
            if (signupRequestDto.getInterests().size() >= 5) {
                throw new IllegalArgumentException("관심사는 최대 4개까지 선택가능합니다.");
            }

            List<Interest> interests = signupRequestDto.getInterests().stream()
                    .distinct()
                    .map(interestName -> new Interest(interestName, user))
                    .collect(Collectors.toList());

            user.setInterests(interests);
        }

        user.setLoginType(LoginType.NORMAL);
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

    @Transactional
    public StatusResponseDto withdrawal(UserDetailsImpl userDetails, String atk) {
        User user = userRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        user.disableUserAccount();

        UserLog userLog = userlogRepository.findByUserId(user.getId()).orElse(null);

        if (userLog != null) {
            UserStatus userStatus = userLog.getStatus();

            if (userStatus == UserStatus.DORMANT) {
                return StatusResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("휴면 계정입니다.")
                        .build();
            } else if (userStatus == UserStatus.SUSPENSION) {
                return StatusResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("정지당한 유저이므로 탈퇴가 불가능합니다.")
                        .build();
            } else if (userStatus == UserStatus.WITHDRAWAL) {
                return StatusResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("이미 탈퇴된 계정입니다.")
                        .build();
            }
        }
        UserLog newUserLog = new UserLog(user, UserStatus.WITHDRAWAL, LocalDateTime.now());
        userlogRepository.save(newUserLog);

        tokenService.invalidateTokensForUser(user, atk);

        return StatusResponseDto.builder().status(HttpStatus.OK).message("회원 탈퇴 성공").build();
    }


    @Transactional
    public UrlResponseDto modifyProfileImage(UserDetailsImpl userDetails, MultipartFile image, String profile) throws IOException {
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        modifyImageValidate(image, profile);
        String imageUrl = saveAndGetNewImageUrl(image, profile);

        deletePreviousImageIfNotDefaultImage(user);
        user.modifyProfileImage(imageUrl);

        return UrlResponseDto.builder()
                .url(imageUrl)
                .build();
    }

    private void deletePreviousImageIfNotDefaultImage(User user) {
        if (!user.getImage().contains(".png")) {
            s3UploadService.deleteImage(user.getImage());
        }
    }

    private String saveAndGetNewImageUrl(MultipartFile image, String profile) throws IOException {
        if (image == null) {
            return s3UploadService.getDefaultImageUrl(profile);

        } else {
            return s3UploadService.saveFile(image);
        }
    }

    private static void modifyImageValidate(MultipartFile image, String profile) {
        if (image == null && profile == null) {
            throw new NullPointerException("등록할 이미지 파일 혹은 기본 이미지 이름 중 하나는 필수입니다.");
        }
    }


    public String setRandomDefaultImageUrl() {
        ArrayList<String> defaultImageKey = new ArrayList<>(List.of("profile1.png", "profile2.png", "profile3.png", "profile4.png", "profile5.png", "profile6.png", "profile7.png"));

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

    public HttpHeaders createTokenHeader(String jwtAccessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("AccessToken", jwtAccessToken);
        headers.add("RefreshToken", refreshToken);
        return headers;
    }

    public int countActiveUser() {
        int activeUser = 0;
        activeUser = refreshTokenRepository.countRefreshTokens();
        return activeUser;
    }

    @Transactional
    public StatusResponseDto rateCleanPoint(User user, String partnerNickname, Long point) {

        if (user.getNickname().equals(partnerNickname)) {
            throw new IllegalArgumentException("본인의 점수는 매길 수 없습니다.");
        }

        User partner = userRepository.findByNickname(partnerNickname).orElseThrow(
                () -> new RuntimeException("존재하지 않는 사용자입니다.")
        );
        partner.updateCleanPoint(point);

        return StatusResponseDto.builder()
                .status(HttpStatus.OK)
                .message(partnerNickname + "님의 점수를 등록했습니다!")
                .build();
    }

    public DashBoardResponseDto getDashBoardInfo(UserDetailsImpl userDetails) {
        User user = userRepository.findByLoginId(userDetails.getUsername()).orElseThrow(() -> new NullPointerException("없는 유저입니다"));

        return DashBoardResponseDto.builder()
                .matchRoomList(matchDataService.getMatchRoomResponseDtos(user))
                .memoList(memoService.getMemoListForDashBoard(user))
                .userCount(countActiveUser())
                .userCountList(hourTrafficService.todayUserCountListByHour())
                .build();
    }

    public DashBoardResponseDto getDashBordData(UserDetailsImpl userDetails) {

        return dashBoardRepository.getDashBoard(userDetails);
    }
    @Transactional
    public StatusResponseDto rejoinStatus(User user) {
        UserLog userLog = userlogRepository.findByUserId(user.getId()).orElse(null);

        if (userLog != null && userLog.getStatus() == UserStatus.WITHDRAWAL) {
            if (userLog.isRejoinAllowed()) {
                userlogRepository.delete(userLog);

                user.enableUserAccount();
                userRepository.save(user);

                return StatusResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message("재가입이 완료되었습니다")
                        .build();
            } else {
                return StatusResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("탈퇴 후 24시간이 지나야 재가입 가능합니다.")
                        .build();
            }
        } else {
            return StatusResponseDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public StatusResponseDto rejoin(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 사용자입니다.")
        );

        return rejoinStatus(user);
    }
}
