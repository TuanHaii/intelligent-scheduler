package com.example.intelligent_scheduler.infrastructure.security;

import com.example.intelligent_scheduler.domain.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class jwtProvider {
    private final JwtEncoder jwtEncoder;
    // Thời gian sống của Access Token (VD: 900s = 15 phút)
    @Value("${app.jwt.expiration-second:900}")
    private Long accessTokenExpiration;

    // Thời gian sống của Refresh Token (VD: 604800s = 7 ngày)
    @Value("${app.jwt.refresh-expiration-seconds:604800}")
    private long refreshTokenExpiration;

    public jwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    public  String generateAccessToken(User user) {
        Instant now = Instant.now();
        // cau hinh noi dung
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("intelligent-scheduler")
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiration, ChronoUnit.SECONDS))
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .build();
        // ma hoa bang thuat toan HS256
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
    public  String generateRefreshToken(User user) {
        Instant now = Instant.now();

        // Refresh Token thiết kế tinh gọn hơn, không cần nhồi nhét Role hay Email
        // Chỉ cần biết nó thuộc về ai (subject) để sau này cấp lại Access Token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("intelligent-scheduler-system")
                .issuedAt(now)
                .expiresAt(now.plus(refreshTokenExpiration, ChronoUnit.SECONDS))
                .subject(user.getId().toString())
                .build();

        JwsHeader jwsHeader =  JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
