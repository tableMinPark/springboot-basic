package com.practice.auth.global.jwt;

import com.practice.auth.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    private String TOKEN;
    private final Long MEMBER_ID = 0L;
    private final String ROLE = "NORMAL";

    @BeforeEach
    void generateToken() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder()
                .role(ROLE)
                .build());

        TOKEN = tokenProvider.generateAccessToken(MEMBER_ID, roleList);
    }

    @DisplayName("JWT 토큰 발급 테스트")
    @Test
    void generateTokenTest() {
        assertNotNull(TOKEN);
    }

    @DisplayName("JWT 토큰 페이 로드 추출 테스트")
    @Test
    void extractPayloadTest() {
        Long memberId = tokenProvider.getMemberId(TOKEN);
        String role = tokenProvider.getRole(TOKEN);
        assertEquals(MEMBER_ID, memberId);
        assertEquals(ROLE, role);
    }
}