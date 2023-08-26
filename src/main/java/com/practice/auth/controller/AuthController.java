package com.practice.auth.controller;

import com.practice.auth.global.code.FailCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.global.response.SuccessResponse;
import com.practice.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerMember(@RequestParam(name = "email") String email,
                                                 @RequestParam(name = "password") String password) throws RuntimeException {
        log.info("registerMember - Call");

        if (email == null || password == null) {
            throw new FailException(FailCode.INVALID_ARGS);
        }

        authService.registerMember(email, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse(null));
    }
}
