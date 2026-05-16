package com.example.intelligent_scheduler.application.auth.service.impl;

import com.example.intelligent_scheduler.application.auth.dto.LoginRequest;
import com.example.intelligent_scheduler.application.auth.dto.RegisterRequest;
import com.example.intelligent_scheduler.application.auth.dto.TokenResponse;
import com.example.intelligent_scheduler.application.auth.service.AuthService;
import com.example.intelligent_scheduler.domain.entity.User;
import com.example.intelligent_scheduler.repository.RefreshTokenRepository;
import com.example.intelligent_scheduler.repository.UserRepository;
import com.example.intelligent_scheduler.domain.entity.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.intelligent_scheduler.infrastructure.security.jwtProvider;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final jwtProvider JwtProvider;

    // REGISTER
    @Override
    @Transactional
    public void register(RegisterRequest request){
        // 1. Kiem tra email
        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        // 2. hash password và tạo entity user
        User newUser = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role("USER") // mặc định role là user
                .isActive(true)
                .build();
        // 3. Lưu user vào database
        userRepository.save(newUser);
    }

    // LOGIN
    @Override
    @Transactional
    public TokenResponse login(LoginRequest request){
        // 1. Kiểm tra User
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không chính xác"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác");
        }
        // 3. Tạo JWT
        String accessToken = JwtProvider.generateAccessToken(user);
        String refreshToken = JwtProvider.generateRefreshToken(user);
        // lưu refresh token vào database
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .isRevoked(false)
                .expiresAt(OffsetDateTime.now().plusDays(7)) // Giả sử 7 ngày
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        System.out.println("Đã lưu Refresh Token mới cho user: {} " + user.getEmail());

        System.out.println("User {} đã đăng nhập thành công " + user.getEmail());
        //4. trả về token
        return new TokenResponse(accessToken, refreshToken,"Bearer", 3600L);
    }
}
