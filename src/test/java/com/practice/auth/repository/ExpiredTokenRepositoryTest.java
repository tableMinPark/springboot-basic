package com.practice.auth.repository;

import com.practice.auth.entity.ExpiredToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpiredTokenRepositoryTest {
    @Autowired
    private ExpiredTokenRepository expiredTokenRepository;

    private final String TOKEN = "token_sample";
    private final Long MEMBER_ID = 1L;

    @BeforeEach
    void registerExpiredToken() {
        ExpiredToken expiredToken = ExpiredToken.builder()
                .token(TOKEN)
                .memberId(1L)
                .build();

        expiredTokenRepository.save(expiredToken);
    }

    @AfterEach
    void deleteExpiredToken() {
        expiredTokenRepository.deleteById(TOKEN);
    }

    @DisplayName("ExpiredToken 생성 테스트")
    @Test
    @Transactional
    void expiredTokenRegisterTest() {
        assertTrue(true);
    }


    @DisplayName("ExpiredToken 조회 테스트")
    @Test
    @Transactional
    void expiredTokenFindTest() {
        Optional<ExpiredToken> op = expiredTokenRepository.findById(TOKEN);
        assertTrue(op.isPresent());

        ExpiredToken expiredToken = op.get();
        assertEquals(TOKEN, expiredToken.getToken());
        assertEquals(MEMBER_ID, expiredToken.getMemberId());
    }
}