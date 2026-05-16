package com.example.intelligent_scheduler.infrastructure.external;

import com.example.intelligent_scheduler.application.common.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendWelcomeEmailAsync(String email, String fullName) {
        try {
            log.info("Bắt đầu gửi email tới: {} trên luồng: {}", email, Thread.currentThread().getName());
            Thread.sleep(3000);
            log.info("Đã gửi email chào mừng thành công tới {}", email);
        } catch (InterruptedException e) {
            log.error("Lỗi khi gửi email", e);
        }
    }

}
