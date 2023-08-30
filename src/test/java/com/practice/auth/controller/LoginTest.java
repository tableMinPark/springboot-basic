package com.practice.auth.controller;

import com.practice.auth.TestUtil;
import com.practice.auth.dto.response.LoginResDto;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.repository.SessionRepository;
import com.practice.auth.service.AuthService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void beforeEach() {
        // 회원 생성
        testUtil.registerMember(RoleCode.NORMAL.code, RoleCode.ADMIN.code);
    }

    @AfterEach
    void afterEach() {
        testUtil.deleteMember();
        testUtil.deleteSession();
    }

    @DisplayName("로그인 단위 테스트")
    @Test
    @Transactional
    void 로그인_단위_테스트() {
        LoginResDto loginResDto = authService.login(testUtil.EMAIL, testUtil.PASSWORD);

        assertNotNull(loginResDto.getAccessToken());
        assertNotNull(loginResDto.getRefreshToken());
    }

    @DisplayName("이메일 유효성 단위 테스트")
    @Test
    @Transactional
    void 이메일_유효성_단위_테스트() {
        String email = "test test.com";
        String password = "12345678";

        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비밀번호 유효성 단위 테스트")
    @Test
    @Transactional
    void 비밀번호_유효성_단위_테스트() {
        String email = "test@test.com";
        String password = "1234";

        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비밀번호 미일치 단위 테스트")
    @Test
    @Transactional
    void 비밀번호_미일치_단위_테스트() {
        String email = "test@test.com";
        String password = "1234567890";

        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }

    @DisplayName("비회원 로그인 단위 테스트")
    @Test
    @Transactional
    void 비회원_로그인_단위_테스트() {
        String email = "test2@test.com";
        String password = "12345678";

        assertThrows(FailException.class, () -> {
            authService.login(email, password);
        });
    }
}
