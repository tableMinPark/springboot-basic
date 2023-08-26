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

    @DisplayName("Session 조회 테스트")
    @Test
    @Transactional
    void sessionFindTest() {
        Optional<Session> op = sessionRepository.findById(MEMBER_ID);
        assertTrue(op.isPresent());

        Session session = op.get();
        assertEquals(ACCESS_TOKEN, session.getAccessToken());
        assertEquals(REFRESH_TOKEN, session.getRefreshToken());
    }

    @DisplayName("Session 수정 테스트")
    @Test
    @Transactional
    void sessionModifyTest() {
        // when
        Optional<Session> op = sessionRepository.findById(MEMBER_ID);
        assertTrue(op.isPresent());

        String modifyAccessToken = "modify_accessToken_sample";
        String modifyRefreshToken = "modify_refreshToken_sample";
        Session session = op.get();
        session.setAccessToken(modifyAccessToken);
        session.setRefreshToken(modifyRefreshToken);
        sessionRepository.save(session);

        // then
        op = sessionRepository.findById(MEMBER_ID);
        session = op.get();
        assertEquals(modifyAccessToken, session.getAccessToken());
        assertEquals(modifyRefreshToken, session.getRefreshToken());
    }

    @DisplayName("Session 삭제 테스트")
    @Test
    @Transactional
    void sessionDeleteTest() {
        sessionRepository.deleteById(MEMBER_ID);

        Optional<Session> op = sessionRepository.findById(MEMBER_ID);
        assertFalse(op.isPresent());
    }
}