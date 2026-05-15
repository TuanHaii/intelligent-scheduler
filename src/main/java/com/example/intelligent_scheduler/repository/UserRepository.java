package com.example.intelligent_scheduler.repository;

import com.example.intelligent_scheduler.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Phục vụ đăng nhập qua spring security
    Optional<User> findByEmail(String email);
    // kiểm tra email tồn tại khi đăng ký
    boolean existsByEmail(String email);
}
