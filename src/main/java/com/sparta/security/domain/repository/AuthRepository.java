package com.sparta.security.domain.repository;

import com.sparta.security.domain.entity.User;

import java.util.Optional;

public interface AuthRepository {
    Optional<User> findByUsername(String username);

    User save(User user);
}
