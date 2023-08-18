package com.teamip.heyhello.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.user.dto.KakaoUserInfoDto;
import com.teamip.heyhello.domain.user.dto.LoginResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import com.teamip.heyhello.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final Random random = new Random();
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<LoginResponseDto> kakaoLogin(String code) throws JsonProcessingException {
        String loginUrl= "http://localhost:8080/api/users/login/kakao";
        String accessToken = getToken(code, loginUrl);
        log.info("Kakao's accessToken : "+accessToken);
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        String jwtAccessToken = jwtUtil.createAccessToken(kakaoUser.getLoginId());

        String jwtrefreshToken = refreshTokenRepository.createAndSave(kakaoUser.getLoginId());

        HttpHeaders headers = userService.createTokenHeader(jwtAccessToken, jwtrefreshToken);
        return ResponseEntity.ok().headers(headers).body(LoginResponseDto.builder().user(kakaoUser).build());
    }

    private String getToken(String code, String loginUrl) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "e246974818ac1703603a86f1db380f07");
        body.add("redirect_uri", loginUrl);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String id = jsonNode.get("id").toString();

        log.info("카카오 사용자 정보: " + id);
        return new KakaoUserInfoDto(id);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        String loginId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByLoginId(loginId).orElse(null);

        if (kakaoUser == null) {
            String kakaoId = kakaoUserInfo.getId();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String nickname = UUID.randomUUID().toString();
            String image = userService.setRandomDefaultImageUrl();

            kakaoUser = User.builder()
                    .loginId(kakaoId)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .image(image)
                    .build();

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    public StatusResponseDto kakaoWithdrawal(UserDetailsImpl userDetails, String token,String atk) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v1/user/unlink")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token);

        RequestEntity<Void> requestEntity = RequestEntity
                .post(uri)
                .headers(headers).build();
        ResponseEntity<KakaoUserInfoDto> response;
        try {
            response = restTemplate.exchange(
                    requestEntity,
                    KakaoUserInfoDto.class
            );
        } catch (Exception e){
            throw new IllegalArgumentException("유효하지 않은 탈퇴 요청입니다.");
        }
        log.info(response.getBody().getId());
        return userService.withdrawal(userDetails, atk);
    }

    public ResponseEntity<StatusResponseDto> getKakaoTokenForWithdrawal(String code) throws JsonProcessingException {
        //TODO 나중에 프론트에서 탈퇴 페이지 URI로 추가 등록해서 교체해야함
        String authUrl= "http://localhost:8080/api/users/withdrawal/social";
        String accessToken = getToken(code, authUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", accessToken);
        return ResponseEntity.ok().headers(headers).build();
    }
}
