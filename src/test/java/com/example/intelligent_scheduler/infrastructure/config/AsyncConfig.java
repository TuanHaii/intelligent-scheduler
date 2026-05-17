package com.example.intelligent_scheduler.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

// xử lý đa luồng và bất đồng bộ
@Configuration
@EnableAsync // bật tính năng đa luồng
public class AsyncConfig {
}
