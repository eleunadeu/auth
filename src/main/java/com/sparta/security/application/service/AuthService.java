package com.sparta.security.application.service;

import com.sparta.security.application.dto.AuthDto;
import com.sparta.security.application.dto.SignInResponse;
import com.sparta.security.application.dto.SignUpResponse;
import com.sparta.security.domain.entity.Role;
import com.sparta.security.domain.entity.User;
import com.sparta.security.domain.repository.AuthRepository;
import com.sparta.security.infrastructure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signUp(AuthDto dto) {
        log.info("회원가입");

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        if (authRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 유저입니다.");
        }

        User user = User.create(dto.getUsername(), encodedPassword, dto.getNickname(), Role.ROLE_USER);
        authRepository.save(user);

        return SignUpResponse.from(user);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(AuthDto dto) {
        log.info("로그인 요청");

        User user = authRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 ID입니다.")
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("유효하지 않은 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole());

        log.info("로그인 성공");

        return SignInResponse.from(accessToken);
    }
}
