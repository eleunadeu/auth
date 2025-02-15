package com.sparta.security.presentation.dto;

import com.sparta.security.application.dto.AuthDto;
import lombok.Getter;

@Getter
public class SignInRequest {

    private String username;
    private String password;

    public AuthDto toDto() {
        return AuthDto.of(this.username, this.password,null);
    }
}