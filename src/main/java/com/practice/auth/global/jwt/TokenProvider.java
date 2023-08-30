package com.practice.auth.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String JWT_KEY;

    @Value("${jwt.expired.access_token}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${jwt.expired.refresh_token}")
    private Long REFRESH_TOKEN_EXPIRED;

    public String generateAccessToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        return generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }

    public String generateRefreshToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        return generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }

    public Long getExpiredSeconds(String token) {
        LocalDateTime expiration = extractAllClaims(token).getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // Return Seconds
        long expired = Duration.between(LocalDateTime.now(), expiration).getSeconds();

        return Math.max(0, expired);
    }

    public Long getMemberId(String token) {
        return extractAllClaims(token).get("memberId", Long.class);
    }

    private Claims extractAllClaims(String token) {
        Key signingKey = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Claims claims, Long expired) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expired * 1000);      // Millis Seconds
        Key signingKey = getSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
