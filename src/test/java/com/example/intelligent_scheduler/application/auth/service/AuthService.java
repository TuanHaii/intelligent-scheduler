package com.example.intelligent_scheduler.application.auth.service;

import com.example.intelligent_scheduler.application.auth.dto.LoginRequest;
import com.example.intelligent_scheduler.application.auth.dto.RegisterRequest;
import com.example.intelligent_scheduler.application.auth.dto.TokenResponse;

// interface Auth Service giúp định nghĩa phương thức  đăng ký và đăng nhập cho người dùng. Các phương thức này sẽ được triển khai trong lớp AuthServiceImpl để xử lý logic liên quan đến xác thực và quản lý token.
public interface AuthService {
    void register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}
