package com.sparta.security.presentation.dto;

import com.sparta.security.application.dto.AuthDto;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @Pattern(
            regexp = "^[a-z0-9]{4,10}$",
            message = "아이디는 4자 이상 10자 이하의 알파벳 소문자와 숫자로 구성되어야 합니다."
    )
    private String username;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+~`|}{\\[\\]:;?><,./-]).{8,15}$",
            message = "비밀번호는 8자 이상 15자 이하의 알파벳, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    private String nickname;

    public AuthDto toDto() {
        return AuthDto.of(this.username, this.password, this.nickname);
    }
}