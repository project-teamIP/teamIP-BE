package com.teamip.heyhello.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamip.heyhello.domain.user.dto.GoogleUserInfoDto;
import com.teamip.heyhello.domain.user.dto.LoginResponseDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.auth.UserDetailsImpl;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.redis.RefreshTokenRepository;
import com.teamip.heyhello.global.redis.TokenResponse;
import com.teamip.heyhello.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

@Slf4j(topic = "GOOGLE Login")
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${social.google.client-id}")
    private String CLIENT_ID;

    @Value("${social.google.client_secret}")
    private String CLIENT_SECRET;

    @Value("${social.google.redirect_uri}")
    private String REDIRECT_URI;

    public ResponseEntity<LoginResponseDto> googleLogin(String code) throws JsonProcessingException {
        String googleaccessToken = getToken(code);
        log.info("google accessToken: " + googleaccessToken);
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(googleaccessToken);

        User googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        String jwtAccessToken = jwtUtil.createAccessToken(googleUser.getLoginId());

        String refreshToken = refreshTokenRepository.createAndSave(googleUser.getLoginId());

        HttpHeaders headers = userService.createTokenHeader(jwtAccessToken, refreshToken);
        return ResponseEntity.ok().headers(headers).body(LoginResponseDto.builder().user(googleUser).build());
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com")
                .path("/oauth2/v4/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
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
    private GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com")
                .path("/oauth2/v3/userinfo")
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
        String id = jsonNode.get("sub").toString();
        return new GoogleUserInfoDto(id);
    }

    private User registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfo) {
        String loginId = googleUserInfo.getId();
        User googleUser = userRepository.findByLoginId(loginId).orElse(null);

        if (googleUser == null) {
            String googleId = googleUserInfo.getId();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String nickname = UUID.randomUUID().toString();
            String image = userService.setRandomDefaultImageUrl();

            googleUser = User.builder()
                    .loginId(googleId)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .image(image)
                    .build();

            userRepository.save(googleUser);
        }
        return googleUser;
    }


    public StatusResponseDto googleWithdrawal(UserDetailsImpl userDetails, String token,String atk) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://accounts.google.com")
                .path("/o/oauth2/revoke")
                .queryParam("token", token)
                .encode()
                .build()
                .toUri();

        restTemplate.postForLocation(uri, null);

        return userService.withdrawal(userDetails, atk);
    }

}
