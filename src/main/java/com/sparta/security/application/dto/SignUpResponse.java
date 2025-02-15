package com.sparta.security.application.dto;

import com.sparta.security.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SignUpResponse {

    private String username;
    private String nickname;
    private List<Map<String, String>> authorities;

    public static SignUpResponse from(User user) {
        return SignUpResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorities(List.of(Map.of("authorityName", user.getRole().toString())))
                .build();
    }
}
