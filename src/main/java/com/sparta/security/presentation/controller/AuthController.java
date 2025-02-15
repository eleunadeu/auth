package com.sparta.security.presentation.controller;

import com.sparta.security.application.dto.SignInResponse;
import com.sparta.security.application.dto.SignUpResponse;
import com.sparta.security.application.service.AuthService;
import com.sparta.security.presentation.dto.SignInRequest;
import com.sparta.security.presentation.dto.SignUpRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 관리", description = "권한 불필요")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest request) {
        log.info("Auth Controller signUp Api Call");

        return authService.signUp(request.toDto());
    }

    @PostMapping("/sign")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest request) {
        log.info("Auth Controller signIn Api Call");

        return authService.signIn(request.toDto());
    }
}
