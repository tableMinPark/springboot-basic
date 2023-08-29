package com.practice.auth.controller;

import com.practice.auth.dto.response.LoginResDto;
import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import com.practice.auth.repository.SessionRepository;
import com.practice.auth.service.AuthService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "12345678";
    private final Long MEMBER_ID = 1L;

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
        // given
        String email = "test@test.com";
        String password = "12345678";

        // when
        LoginResDto loginResDto = authService.login(email, password);

        // then
        assertNotNull(loginResDto.getAccessToken());
        assertNotNull(loginResDto.getRefreshToken());
        sessionRepository.deleteById(MEMBER_ID);
    }

    @DisplayName("이메일 유효성 단위 테스트")
    @Test
    @Transactional
    void 이메일_유효성_단위_테스트() {
        // given
        String email = "test test.com";
        String password = "12345678";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비밀번호 유효성 단위 테스트")
    @Test
    @Transactional
    void 비밀번호_유효성_단위_테스트() {
        // given
        String email = "test@test.com";
        String password = "1234";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비밀번호 미일치 단위 테스트")
    @Test
    @Transactional
    void 비밀번호_미일치_단위_테스트() {
        // given
        String email = "test@test.com";
        String password = "1234567890";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비회원 로그인 단위 테스트")
    @Test
    @Transactional
    void 비회원_로그인_단위_테스트() {
        // given
        String email = "test2@test.com";
        String password = "12345678";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }
}
