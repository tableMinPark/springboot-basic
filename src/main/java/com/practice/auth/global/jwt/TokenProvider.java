package com.practice.auth.global.jwt;

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
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.secret}")
    private String JWT_KEY;

    @Value("${jwt.expired.access_token}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${jwt.expired.refresh_token}")
    private Long REFRESH_TOKEN_EXPIRED;

    public String generateAccessToken(Long memberId, String role) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("role", role);
        return generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }

    public String generateRefreshToken(Long memberId, String role) {
        Claims claims = Jwts.claims();
        return generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }

    public Long getMemberId(String token) {
        return extractAllClaims(token).get("memberId", Long.class);
    }

    public String getRole(String token) {
        return extractAllClaims(token).get("role", String.class);
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
        Date expiration = new Date(now + expired);
        Key signingKey = getSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
