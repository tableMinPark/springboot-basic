package com.practice.auth.global.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러", 500);

    public final HttpStatus httpStatus;
    public final String message;
    public final Integer code;
}
