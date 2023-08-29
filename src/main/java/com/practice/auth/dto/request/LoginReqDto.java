package com.practice.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReqDto {
    private String email;
    private String password;
}
