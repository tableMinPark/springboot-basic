package com.practice.auth.global.exception;

import com.practice.auth.global.code.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorException extends RuntimeException {
    public final ErrorCode errorCode;
}
