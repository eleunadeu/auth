package com.sparta.security.infrastructure.repository;

import com.sparta.security.domain.entity.User;
import com.sparta.security.domain.repository.AuthRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<User, Long>, AuthRepository {
}
