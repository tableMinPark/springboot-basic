package com.practice.auth.api;

import com.google.gson.Gson;
import com.practice.auth.dto.request.RegisterMemberReqDto;
import com.practice.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class RegisterMemberTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    @DisplayName("회원 가입 단위 테스트")
    @Test
    void 회원_가입_유효성_단위_테스트() throws Exception {
        // given
        String email = "test@test.com";
        String password = "12345678";
        authService.registerMember(email, password);

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
}