package com.example.intelligent_scheduler.application.user.dto;

import java.time.LocalTime;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String fullName,
        String email,
        String role,
        LocalTime workingHourStart,
        LocalTime workingHourEnd,
        String timezone,
        Boolean isEmailVerified
) {
}
