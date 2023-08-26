package com.practice.auth.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleCode {
    NORMAL("NORMAL", "일반 회원"),
    COMPANY("COMPANY", "기업 회원"),
    ADMIN("ADMIN", "관리자");

    public final String code;
    public final String name;
}
