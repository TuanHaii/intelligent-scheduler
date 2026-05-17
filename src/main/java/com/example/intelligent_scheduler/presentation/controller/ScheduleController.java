package com.example.intelligent_scheduler.presentation.controller;

import com.example.intelligent_scheduler.application.schedule.dto.ScheduleRequest;
import com.example.intelligent_scheduler.application.schedule.dto.ScheduleResponse;
import com.example.intelligent_scheduler.application.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    private UUID getUserId(Jwt jwt){
        return UUID.fromString(jwt.getClaimAsString("sub"));
    }

    // tao moi schedule
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ScheduleRequest Request){
        UUID userId = getUserId(jwt);
        ScheduleResponse scheduleResponse = scheduleService.createSchedule(userId, Request);
        return ResponseEntity.ok(scheduleResponse);
    }

    // get schedule
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)OffsetDateTime end){
        UUID userId = getUserId(jwt);
        List<ScheduleResponse> schedules = scheduleService.getSchedules(userId, start, end);
        return ResponseEntity.ok(schedules);
    }
}
