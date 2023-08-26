package com.practice.auth.api;

import com.google.gson.Gson;
import com.practice.auth.dto.request.RegisterMemberReqDto;
import com.practice.auth.global.code.FailCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegisterMemberTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원 가입 단위 테스트")
    @Test
    void 회원_가입_단위_테스트() throws Exception {
        // given
        String email = "test@test.com";
        String password = "12345678";

        RegisterMemberReqDto request = RegisterMemberReqDto.builder()
                .email(email)
                .password(password)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @DisplayName("이메일 미입력 단위 테스트")
    @Test
    void 이메일_미입력_단위_테스트() throws Exception {
        // given
        String password = "12345678";

        RegisterMemberReqDto request = RegisterMemberReqDto.builder()
                .password(password)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        // then
        FailCode failCode = FailCode.INVALID_ARGS;

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.data.title", is(failCode.title)))
                .andExpect(jsonPath("$.data.content", is(failCode.content)));
    }

    @DisplayName("비밀번호 미입력 단위 테스트")
    @Test
    void 비밀번호_미입력_단위_테스트() throws Exception {
        // given
        String email = "test@test.com";

        RegisterMemberReqDto request = RegisterMemberReqDto.builder()
                .email(email)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        // then
        FailCode failCode = FailCode.INVALID_ARGS;

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.data.title", is(failCode.title)))
                .andExpect(jsonPath("$.data.content", is(failCode.content)));
    }
}