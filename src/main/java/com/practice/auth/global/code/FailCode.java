package com.practice.auth.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FailCode {
    INVALID_EMAIL("이메일 유효성 확인", "유효하지 않은 이메일 입니다."),
    INVALID_PASSWORD("비밀번호 유효성 확인", "유효하지 않은 비밀번호 입니다."),
    DUPLICATE_MEMBER("회원 정보 중복", "이메일과 일치하는 회원 정보가 존재합니다.");

    public final String title;
    public final String content;
}
