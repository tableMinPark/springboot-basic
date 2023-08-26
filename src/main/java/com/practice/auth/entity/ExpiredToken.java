package com.practice.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@RedisHash("expiredToken")
@AllArgsConstructor
public class ExpiredToken {
    @Id
    private String token;
    private Long memberId;
    @TimeToLive
    private Long expire;
}
