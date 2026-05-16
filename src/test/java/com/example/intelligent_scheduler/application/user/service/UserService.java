package com.example.intelligent_scheduler.application.user.service;

import com.example.intelligent_scheduler.application.user.dto.UpdatePreferencesRequest;
import com.example.intelligent_scheduler.application.user.dto.UserProfileResponse;

import java.util.UUID;

public interface UserService {
    UserProfileResponse getUserProfile(UUID userId);
    void updatePreferences(UUID userId, UpdatePreferencesRequest request);
}
