package com.practice.auth.global.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SuccessBasicResponse extends BasicResponse {
    private final Object data;
    public SuccessBasicResponse(Object data) {
        super("success");
        this.data = data;
    }
}
