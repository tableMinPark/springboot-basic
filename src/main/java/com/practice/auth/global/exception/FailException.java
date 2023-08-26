package com.practice.auth.global.exception;

import com.practice.auth.global.code.FailCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FailException extends RuntimeException {
    public final FailCode failCode;
}
