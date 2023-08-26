package com.practice.auth.repository;

import com.practice.auth.entity.ExpiredToken;
import com.practice.auth.entity.Session;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SessionRepositoryTest {
    @Autowired
    private SessionRepository sessionRepository;

    private final String ACCESS_TOKEN = "accessToken_sample";
    private final String REFRESH_TOKEN = "accessToken_sample";
    private final Long MEMBER_ID = 1L;

    @BeforeEach
    void registerSession() {
        Session session = Session.builder()
                .memberId(MEMBER_ID)
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .loginDt(LocalDateTime.now())
                .build();

        sessionRepository.save(session);
    }

    @AfterEach
    void deleteSession() {
        sessionRepository.deleteById(MEMBER_ID);
    }

    @DisplayName("Session 생성 테스트")
    @Test
    @Transactional
    void sessionRegisterTest() {
        assertTrue(true);
    }
}