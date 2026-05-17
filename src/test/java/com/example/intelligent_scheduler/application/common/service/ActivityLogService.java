package com.example.intelligent_scheduler.application.common.service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

// cac hoat dong nhu ghi, luu log, gui email, ...
@Slf4j
@Service
public class ActivityLogService {
    @Async // day ham nay sang Thread khac de thuc hien, khong lam tre main thread
    public void logUserActivityAsync(UUID userId, String action) {
        try {
            log.info("[Thread: {}] Đang ghi nhận hoạt động: User {} vừa {}",
                    Thread.currentThread().getName(), userId, action);

            // Giả lập độ trễ khi lưu vào bảng activity_logs trong DB
            Thread.sleep(1000);

            log.info("Đã lưu nhật ký hoạt động thành công!");
        } catch (InterruptedException e) {
            log.error("Lỗi khi ghi log hoạt động", e);
        }
    }

}
