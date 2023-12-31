package com.practice.auth.global.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum FailCode {
    NOT_FOUND_SESSION(HttpStatus.BAD_REQUEST, "로그인하지 않은 회원", "로그인 기록이 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "등록되지 않은 회원", "회원가입이 필요합니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 유효성 확인", "유효하지 않은 이메일 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 유효성 확인", "유효하지 않은 비밀번호 입니다."),
    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "회원 정보 중복", "이메일과 일치하는 회원 정보가 존재합니다."),
    INVALID_ARGS(HttpStatus.BAD_REQUEST, "입력 값 유효성 확인", "입력 되지 않은 값이 있습니다.");

    public final HttpStatus httpStatus;
    public final String title;
    public final String content;
}
