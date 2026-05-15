package com.example.intelligent_scheduler.application.common.dto;

import java.time.OffsetDateTime;

// DTO for error responses
public record ErrorResponse(
        int status,
        String error,
        String message,
        OffsetDateTime timestamp
) {
}
