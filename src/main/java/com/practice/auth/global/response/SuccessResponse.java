package com.practice.auth.global.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SuccessResponse extends BasicResponse {
    private final Object data;
    public SuccessResponse(Object data) {
        super("success");
        this.data = data;
    }
}
