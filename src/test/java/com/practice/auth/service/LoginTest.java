package com.practice.auth.service;

import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.entity.Session;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.jwt.TokenProvider;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import com.practice.auth.repository.SessionRepository;
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

import javax.persistence.EntityManager;
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
    private EntityManager entityManager;
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

    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "12345678";

    @BeforeEach
    void register() {
        // 회원 생성
        this.entityManager.createNativeQuery("ALTER TABLE member ALTER member_id RESTART WITH 1").executeUpdate();
        String password = passwordEncoder.encode(PASSWORD);
        Member member = Member.builder()
                .email(EMAIL)
                .password(password)
                .build();

        memberRepository.save(member);

        // 회원 역할 생성
        this.entityManager.createNativeQuery("ALTER TABLE role ALTER role_id RESTART WITH 1").executeUpdate();
        roleRepository.save(Role.builder()
                .memberId(member.getMemberId())
                .role(RoleCode.NORMAL.code)
                .build());
    }

    @DisplayName("로그인 단위 테스트")
    @Test
    @Transactional
    void 로그인_단위_테스트() {
        // when
        /* 회원 정보 유무 확인 */
        Optional<Member> op = memberRepository.findByEmail(EMAIL);
        if (!op.isPresent()) {
            fail();
        }

        /* 비밀번호 일치 여부 확인 */
        Member member = op.get();

        Long memberId = member.getMemberId();
        String password = member.getPassword();

        if (!passwordEncoder.matches(PASSWORD, password)) {
            fail();
        }

        /* Redis(MemberToken) 조회해서 있으면 그대로 반환 */
        Session session = sessionRepository.findById(memberId)
                .orElseGet(() -> {
                    /* 엑세스 토큰 발급 */
                    List<Role> roleList = roleRepository.findByMemberId(memberId);

                    String accessToken = tokenProvider.generateAccessToken(memberId, roleList);
                    String refreshToken = tokenProvider.generateRefreshToken(memberId);

                    Session generateSession = Session.builder()
                                    .memberId(memberId)
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .loginDt(LocalDateTime.now())
                                    .build();

                    /* Redis(MemberToken) 등록 */
                    sessionRepository.save(generateSession);

                    return generateSession;
                });

        /* 토큰 반환 */
        assertNotNull(session);

        // then
        /* Redis(MemberToken) 등록 유무 확인 */
        Optional<Session> so = sessionRepository.findById(memberId);
        assertTrue(so.isPresent());
        sessionRepository.deleteById(memberId);
    }
}