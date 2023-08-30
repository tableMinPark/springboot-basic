package com.practice.auth.service;

import com.practice.auth.TestUtil;
import com.practice.auth.entity.ExpiredToken;
import com.practice.auth.entity.Session;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.jwt.TokenProvider;
import com.practice.auth.repository.ExpiredTokenRepository;
import com.practice.auth.repository.SessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LogoutTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ExpiredTokenRepository expiredTokenRepository;
    @Autowired
    private TokenProvider tokenProvider;

    @Value("${jwt.expired.access_token}")
    private Long ACCESS_TOKEN_EXPIRED;
    @Value("${jwt.expired.refresh_token}")
    private Long REFRESH_TOKEN_EXPIRED;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        testUtil.registerMember(RoleCode.NORMAL.code);
        // 세션 생성
        testUtil.registerSession();
    }

    @AfterEach
    void afterEach() {
        testUtil.deleteMember();
        testUtil.deleteSession();
        testUtil.deleteExpireToken();
    }

    @DisplayName("로그아웃 단위 테스트")
    @Test
    @Transactional
    void 로그아웃_단위_테스트() throws InterruptedException {
        /* Redis(MemberToken) 조회해서 만료가 되지 않은 엑세스, 래프레시 토큰 Redis(ExpiredToken) 에 등록 */
        Optional<Session> so = sessionRepository.findById(testUtil.MEMBER_ID);
        if (so.isEmpty()) {
            fail();
        }

        Session session = so.get();
        ExpiredToken expiredAccessToken = getExpiredToken(session.getAccessToken());
        expiredTokenRepository.save(expiredAccessToken);
        ExpiredToken expiredRefreshToken = getExpiredToken(session.getRefreshToken());
        expiredTokenRepository.save(expiredRefreshToken);

        /* Redis(MemberToken) 삭제 */
        sessionRepository.deleteById(session.getMemberId());

        /* Redis(MemberToken) 삭제 여부 확인 */
        assertTrue(expiredTokenRepository.findById(session.getAccessToken()).isPresent());
        assertTrue(expiredTokenRepository.findById(session.getRefreshToken()).isPresent());
        assertFalse(sessionRepository.existsById(session.getMemberId()));

        /* 만료 여부 확인 */
        Thread.sleep(ACCESS_TOKEN_EXPIRED * 1000);
        assertFalse(expiredTokenRepository.findById(session.getAccessToken()).isPresent());
        Thread.sleep((REFRESH_TOKEN_EXPIRED - ACCESS_TOKEN_EXPIRED) * 1000);
        assertFalse(expiredTokenRepository.findById(session.getRefreshToken()).isPresent());
    }

    private ExpiredToken getExpiredToken(String token) {
        Long expired = tokenProvider.getExpiredSeconds(token);
        Long memberId = tokenProvider.getMemberId(token);

        return ExpiredToken.builder()
                .token(token)
                .memberId(memberId)
                .expire(expired)
                .build();
    }
}