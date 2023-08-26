package com.practice.auth.global.response;

import com.practice.auth.global.code.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse extends BasicResponse {
    private final String message;
    private final Integer code;

    public ErrorResponse(ErrorCode errorCode) {
        super("error");
        this.message = errorCode.message;
        this.code = errorCode.code;
    }
}