package com.practice.auth.controller;

import com.practice.auth.global.code.ErrorCode;
import com.practice.auth.global.code.FailCode;
import com.practice.auth.global.exception.ErrorException;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.global.response.ErrorResponse;
import com.practice.auth.global.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler({ FailException.class })
    private ResponseEntity<Object> handleFailException(FailException e) {
        FailCode failCode = e.failCode;

        log.info("handleFailException : {} - {}", failCode.title, failCode.content);

        return ResponseEntity
                .status(failCode.httpStatus)
                .body(new FailResponse(failCode.title, failCode.content));
    }

    @ExceptionHandler({ ErrorException.class })
    private ResponseEntity<Object> handleErrorException(ErrorException e) {
        ErrorCode errorCode = e.errorCode;

        log.info("handleFailException : {} - {}", errorCode.message, errorCode.code);

        return ResponseEntity
                .status(errorCode.httpStatus)
                .body(new ErrorResponse(errorCode.message, errorCode.code));
    }

    @ExceptionHandler({ RuntimeException.class })
    private ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER;

        log.info("handleRuntimeException : {} - {} - {}", errorCode.message, errorCode.code, e.getMessage());

        return ResponseEntity
                .status(errorCode.httpStatus)
                .body(new ErrorResponse(errorCode.message, errorCode.code));
    }
}
