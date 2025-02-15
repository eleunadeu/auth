package com.sparta.security.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthDto {

    private String username;
    private String password;
    private String nickname;

    public static AuthDto of(String username, String password, String nickname) {
        return AuthDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
