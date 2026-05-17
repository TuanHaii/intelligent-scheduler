package com.example.intelligent_scheduler.repository;

import com.example.intelligent_scheduler.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // Xóa bỏ các token cũ của user khi họ login mới (tùy chọn chính sách bảo mật)
    void deleteByUserId(UUID userId);
}