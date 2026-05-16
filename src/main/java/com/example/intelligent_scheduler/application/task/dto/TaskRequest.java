package com.example.intelligent_scheduler.application.task.dto;

import com.example.intelligent_scheduler.domain.enums.Priority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record TaskRequest(
    @NotBlank(message = "Tiêu đề công việc không được để trống")
    String title,

    String description,

    @NotNull(message = "Ngày hết hạn không được để trống")
    Priority priority,

    @NotNull(message = "Thời gian dự kiến không được để trống")
    @Min(value = 5, message = "Thời gian dự kiến ít nhất phải là 5 phút")
    Integer estimatedDurationMinutes,

    // Deadline có thể null (công việc không có hạn chót)
    OffsetDateTime deadline

) {
}
