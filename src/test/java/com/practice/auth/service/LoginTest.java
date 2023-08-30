package com.practice.auth.service;

import com.practice.auth.TestUtil;
import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.entity.Session;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.jwt.TokenProvider;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import com.practice.auth.repository.SessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoginTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        testUtil.registerMember(RoleCode.NORMAL.code, RoleCode.ADMIN.code);
    }

    @AfterEach
    void afterEach() {
        // 회원 삭제
        testUtil.deleteMember();
    }

    @DisplayName("로그인 단위 테스트")
    @Test
    @Transactional
    void 로그인_단위_테스트() {
        /* 회원 정보 유무 확인 */
        Optional<Member> op = memberRepository.findByEmail(testUtil.EMAIL);
        if (op.isEmpty()) {
            fail();
        }

        /* 비밀번호 일치 여부 확인 */
        Member member = op.get();

        Long memberId = member.getMemberId();
        String password = member.getPassword();

        if (!passwordEncoder.matches(testUtil.PASSWORD, password)) {
            fail();
        }

        /* Redis(MemberToken) 조회해서 있으면 그대로 반환 */
        Session session = sessionRepository.findById(memberId)
                .orElseGet(() -> {
                    /* 엑세스 토큰 발급 */
                    String accessToken = tokenProvider.generateAccessToken(memberId);
                    String refreshToken = tokenProvider.generateRefreshToken(memberId);
                    Long expired = tokenProvider.getExpiredSeconds(refreshToken);
                    Session generateSession = Session.builder()
                                    .memberId(memberId)
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .loginDt(LocalDateTime.now())
                                    .expire(expired)
                                    .build();

                    /* Redis(MemberToken) 등록 */
                    sessionRepository.save(generateSession);

                    return generateSession;
                });

        /* 토큰 반환 */
        assertNotNull(session);

        /* Redis(MemberToken) 등록 유무 확인 */
        Optional<Session> so = sessionRepository.findById(memberId);
        assertTrue(so.isPresent());
        sessionRepository.deleteById(memberId);
    }
}