package com.practice.auth.global.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse extends BasicResponse {
    private final String message;
    private final Integer code;

    public ErrorResponse(String message, Integer code) {
        super("error");
        this.message = message;
        this.code = code;
    }
}
