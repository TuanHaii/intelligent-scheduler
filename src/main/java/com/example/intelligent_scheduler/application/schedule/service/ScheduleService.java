package com.example.intelligent_scheduler.application.schedule.service;

import com.example.intelligent_scheduler.application.auth.dto.LoginRequest;
import com.example.intelligent_scheduler.application.auth.dto.RegisterRequest;
import com.example.intelligent_scheduler.application.auth.dto.TokenResponse;
import com.example.intelligent_scheduler.application.schedule.dto.ScheduleRequest;
import com.example.intelligent_scheduler.application.schedule.dto.ScheduleResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

// interface Auth Service giúp định nghĩa phương thức  đăng ký và đăng nhập cho người dùng. Các phương thức này sẽ được triển khai trong lớp AuthServiceImpl để xử lý logic liên quan đến xác thực và quản lý token.
public interface ScheduleService {
    ScheduleResponse createSchedule(UUID userId, ScheduleRequest request );
    List<ScheduleResponse> getSchedules(UUID userId, OffsetDateTime start, OffsetDateTime end);
}
