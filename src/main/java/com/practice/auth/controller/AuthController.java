package com.practice.auth.controller;

import com.practice.auth.dto.request.LoginReqDto;
import com.practice.auth.dto.request.RegisterMemberReqDto;
import com.practice.auth.global.code.FailCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.global.response.SuccessResponse;
import com.practice.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerMember(@RequestBody RegisterMemberReqDto registerMemberReqDto) throws RuntimeException {
        log.info("registerMember - Call");

        String email = registerMemberReqDto.getEmail();
        String password = registerMemberReqDto.getPassword();

        if (email == null || password == null) {
            throw new FailException(FailCode.INVALID_ARGS);
        }

        authService.registerMember(email, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(null));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginReqDto loginReqDto) throws RuntimeException {
        log.info("login - Call");

        String email = loginReqDto.getEmail();
        String password = loginReqDto.getPassword();

        if (email == null || password == null) {
            throw new FailException(FailCode.INVALID_ARGS);
        }

        authService.login(email, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(null));
    }
}
