package com.example.intelligent_scheduler.application.schedule.dto;

import java.time.OffsetDateTime;

public record ScheduleResponse(
    Long id,
    Long taskId,
    String taskTitle, // lay luon ten task
    OffsetDateTime startTime,
    OffsetDateTime endTime
) {

}
