package com.example.intelligent_scheduler.application.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;
import java.util.UUID;

public record UpdatePreferencesRequest(
        // Ép Jackson (thư viện parse JSON của Spring) phải nhận đúng định dạng giờ
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime workingHourStart,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime workingHourEnd,

        // Dùng Regex để ép định dạng Timezone chuẩn (VD: Asia/Ho_Chi_Minh)
        @Pattern(regexp = "^[A-Za-z]+/[A-Za-z_]+$", message = "Timezone không hợp lệ. Vui lòng dùng định dạng như Khu_Vuc/Thanh_Pho (VD: Asia/Ho_Chi_Minh)")
        String timezone
) {
}
