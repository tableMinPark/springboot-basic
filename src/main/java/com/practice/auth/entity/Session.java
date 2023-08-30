package com.practice.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@Data
@Builder
@RedisHash("session")
@AllArgsConstructor
public class Session {
    @Id
    private Long memberId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime loginDt;
    @TimeToLive
    private Long expire;
}
