package com.example.intelligent_scheduler.application.task.dto;

import com.example.intelligent_scheduler.domain.enums.Priority;
import com.example.intelligent_scheduler.domain.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Priority priority,
        TaskStatus status,
        Integer estimatedDuration,
        OffsetDateTime createdAt,
        OffsetDateTime deadline
) {
}
