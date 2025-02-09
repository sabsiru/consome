package com.consome.auth;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh.expiration}") long refreshTokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * ✅ Access Token 생성
     */
    public String generateAccessToken(String loginId) {
        return createToken(loginId, accessTokenExpiration);
    }

    /**
     * ✅ Refresh Token 생성
     */
    public String generateRefreshToken(String loginId) {
        return createToken(loginId, refreshTokenExpiration);
    }

    /**
     * ✅ JWT 생성 (공통 메서드)
     */
    private String createToken(String loginId, long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * ✅ JWT 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT 형식이 잘못됨: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    /**
     * ✅ JWT에서 로그인 ID 추출
     */
    public String getLoginIdFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료 - 로그인 ID 추출 불가: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.warn("JWT에서 로그인 ID 추출 실패: {}", e.getMessage());
            return null;
        }
    }
}