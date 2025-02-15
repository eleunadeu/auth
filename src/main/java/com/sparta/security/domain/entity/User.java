package com.sparta.security.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "role", nullable = false)
    private Role role;

    public static User create(String username, String password, String nickname, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
