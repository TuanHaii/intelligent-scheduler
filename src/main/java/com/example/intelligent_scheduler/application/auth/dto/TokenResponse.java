package com.example.intelligent_scheduler.application.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType, // Thường là "Bearer"
        Long expiresIn // Thời gian hết hạn (giây)
) {
}
