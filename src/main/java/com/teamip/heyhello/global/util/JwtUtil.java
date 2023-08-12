package com.teamip.heyhello.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil implements InitializingBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    // 1000 = 1초
    // 1000 * 60 = 60초 = 1분
    // 1000 * 60 * 60 = 3600초 = 1시간
    // 1000 * 60 * 60 * 24 = 24시간 = 하루

    private static final long ATK_TIME = 1000L * 60 * 60;

//    private static final long RTK_TIME = 1000L * 60 * 60 * 24;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(System.currentTimeMillis() + ATK_TIME))
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰만료");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            log.error("validate token failed");
        }
        return false;
    }

    public String getSubstringTokenFromRequest(HttpServletRequest req){
        String token = req.getHeader(AUTHORIZATION_HEADER);
        return substringToken(token);
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getLoginIdFromToken(String token) {
        validateToken(token);
        return getUserInfoFromToken(token).getSubject();
    }

    public String substringToken(String token) {
        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        log.info("Not Found Token");
        return null;
    }

    public long getRemainingSeconds(String jwt) {
        String token = substringToken(jwt);
        Jws<Claims> jws = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token);
        Claims claims = jws.getBody();

        long expTime = claims.get("exp", Long.class);
        long currentTime = Instant.now().getEpochSecond();

        return expTime - currentTime;
    }

}

