package com.example.intelligent_scheduler.application.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ScheduleRequest (
    // taskId can null if đây là sự kiện tạo tự do
    Long taskId,

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    OffsetDateTime startTime,

    @NotNull(message = "Thời gian kết thúc không được để trống")
    OffsetDateTime endTime
){
}
