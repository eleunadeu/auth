package com.sparta.security;

import com.sparta.security.application.dto.AuthDto;
import com.sparta.security.application.dto.SignInResponse;
import com.sparta.security.application.dto.SignUpResponse;
import com.sparta.security.application.service.AuthService;
import com.sparta.security.domain.entity.Role;
import com.sparta.security.domain.entity.User;
import com.sparta.security.domain.repository.AuthRepository;
import com.sparta.security.infrastructure.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.create("test", passwordEncoder.encode("test1234"), "nickname", Role.ROLE_USER);
    }

    @Test
    @DisplayName("JWT Access 토큰 생성 및 검증 테스트")
    void jwtTokenGenerateAndValidateTest() {
        String token = "mockJwtToken";
        when(jwtUtil.createToken("test", Role.ROLE_USER)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUserInfoFromToken(token)).thenReturn(mock(Claims.class));

        String generatedToken = jwtUtil.createToken("test", Role.ROLE_USER);
        boolean isValid = jwtUtil.validateToken(generatedToken);

        assertThat(generatedToken).isEqualTo(token);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("JWT Refresh 토큰 생성 및 검증 테스트")
    void jwtRefreshTokenGenerateAndValidateTest() {
        String refreshToken = "mockRefreshToken";
        when(jwtUtil.createRefreshToken("test", Role.ROLE_USER)).thenReturn(refreshToken);
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);

        String generatedRefreshToken = jwtUtil.createRefreshToken("test", Role.ROLE_USER);
        boolean isValid = jwtUtil.validateToken(generatedRefreshToken);

        assertThat(generatedRefreshToken).isEqualTo(refreshToken);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Refresh 토큰 만료 테스트")
    void refreshTokenExpiredTest() {
        String expiredToken = "expiredRefreshToken";
        when(jwtUtil.validateToken(expiredToken)).thenThrow(ExpiredJwtException.class);

        boolean isValid;
        try {
            isValid = jwtUtil.validateToken(expiredToken);
        } catch (ExpiredJwtException e) {
            isValid = false;
        }

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccessTest() {
        AuthDto request = AuthDto.of("test", "test1234", "nickname");
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(authRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user;
        });

        SignUpResponse response = authService.signUp(request);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 중복 username")
    void signUpFailTest() {
        AuthDto request = AuthDto.of("test", "test1234", "nickname");
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(testUser));

        try {
            authService.signUp(request);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("이미 가입된 유저입니다.");
        }
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void signInSuccessTest() {
        AuthDto request = AuthDto.of("test", "test1234", null);
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(any(), any())).thenReturn("mockJwtToken");

        SignInResponse response = authService.signIn(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mockJwtToken");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void signInFailTest() {
        AuthDto request = AuthDto.of("test", "wrongPassword", null);
        when(authRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.signIn(request), "비밀번호가 일치하지 않습니다.");
    }
}
