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
    @AllArgsConstructor
    private static class FailData {
        private String title;
        private String content;
    }
}