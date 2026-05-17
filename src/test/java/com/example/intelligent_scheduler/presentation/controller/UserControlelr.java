package com.example.intelligent_scheduler.presentation.controller;

import com.example.intelligent_scheduler.application.user.dto.UpdatePreferencesRequest;
import com.example.intelligent_scheduler.application.user.dto.UserProfileResponse;
import com.example.intelligent_scheduler.application.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControlelr {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
    // lấy UUID trực tiếp từ Subject của Token
        // không bắt user truyền id trên URL nữa, tránh bị lộ thông tin và bị tấn công
        UUID userId = UUID.fromString(jwt.getSubject());
        UserProfileResponse userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/me/preferences")
    public ResponseEntity<String> updatePreferences(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdatePreferencesRequest request)
    {
        UUID userId = UUID.fromString(jwt.getSubject());
        userService.updatePreferences(userId, request);
        return ResponseEntity.ok("Cập nhật cấu hình thành công!");
    }
}
