package com.example.intelligent_scheduler.presentation.controller;

import com.example.intelligent_scheduler.application.auth.dto.LoginRequest;
import com.example.intelligent_scheduler.application.auth.dto.RegisterRequest;
import com.example.intelligent_scheduler.application.auth.dto.TokenResponse;
import com.example.intelligent_scheduler.application.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        // tra ve HTTP status 201 created
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Đăng ký tài khoản thành công! Hệ thống đang gửi email chào mừng.");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }
}
