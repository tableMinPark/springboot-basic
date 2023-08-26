package com.practice.auth.global.response;

import lombok.*;

@Getter
@ToString
public class FailResponse extends BasicResponse {
    private final FailData data;

    public FailResponse(String title, String content) {
        super("fail");
        this.data = FailData.builder()
                .title(title)
                .content(content)
                .build();
    }

    @Builder
    private static class FailData {
        public String title;
        public String content;
    }
}
