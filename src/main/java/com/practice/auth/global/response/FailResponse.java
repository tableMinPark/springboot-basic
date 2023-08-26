package com.practice.auth.global.response;

import com.practice.auth.global.code.FailCode;
import lombok.*;

@Getter
@ToString
public class FailResponse extends BasicResponse {
    private final FailData data;

    public FailResponse(FailCode failCode) {
        super("fail");
        this.data = FailData.builder()
                .title(failCode.title)
                .content(failCode.content)
                .build();
    }

    @Builder
    private static class FailData {
        public String title;
        public String content;
    }
}
