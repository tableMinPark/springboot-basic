package com.practice.auth.controller;

import com.practice.auth.global.exception.FailException;
import com.practice.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegisterMemberTest {
    @Autowired
    private AuthService authService;

    @DisplayName("회원 가입 단위 테스트")
    @Test
    void 회원_가입_단위_테스트() {
        // given
        String email = "test@test.com";
        String password = "12345678";

        // when & then
        authService.registerMember(email, password);
    }

    @DisplayName("이메일 유효성 단위 테스트")
    @Test
    void 이메일_유효성_단위_테스트() {
        // given
        String email = "test test.com";
        String password = "12345678";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.registerMember(email, password);
        });
    }

    @DisplayName("비밀번호 유효성 단위 테스트")
    @Test
    void 비밀번호_유효성_단위_테스트() {
        // given
        String email = "test@test.com";
        String password = "1234";

        // when & then
        assertThrows(FailException.class, () -> {
            authService.registerMember(email, password);
        });
    }
}