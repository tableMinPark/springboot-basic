package com.practice.auth.controller;

import com.practice.auth.TestUtil;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogoutTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void beforeEach() {
        testUtil.registerMember(RoleCode.NORMAL.code, RoleCode.ADMIN.code);
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
    void 로그아웃_단위_테스트() {
        authService.logout(testUtil.MEMBER_ID);
    }
}
