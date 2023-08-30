package com.practice.auth.api;

import com.practice.auth.TestUtil;
import com.practice.auth.global.code.RoleCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogoutTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MockMvc mockMvc;

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
    @WithUserDetails(value = "1", userDetailsServiceBeanName = "customUserDetailService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void 로그아웃_단위_테스트() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/logout"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", nullValue()));
    }
}
