package com.example.intelligent_scheduler.application.common.service;

public interface EmailService {
    void sendWelcomeEmailAsync(String email, String fullName);
}
