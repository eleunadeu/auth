package com.sparta.security.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {
    private String token;

    public static SignInResponse from(String token) {
        return SignInResponse.builder()
                .token(token)
                .build();
    }
}
