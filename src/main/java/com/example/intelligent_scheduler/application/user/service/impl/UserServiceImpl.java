package com.example.intelligent_scheduler.application.user.service.impl;

import com.example.intelligent_scheduler.application.common.service.ActivityLogService;
import com.example.intelligent_scheduler.application.user.dto.UpdatePreferencesRequest;
import com.example.intelligent_scheduler.application.user.dto.UserProfileResponse;
import com.example.intelligent_scheduler.application.user.mapper.UserMapper;
import com.example.intelligent_scheduler.application.user.service.UserService;
import com.example.intelligent_scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ActivityLogService activityLogService;

    // get user by id
    @Override
    public UserProfileResponse getUserProfile(UUID userId) {
        // tim user theo UUID duoc lay trong token
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // map entity sang DTO va tra ve
        return userMapper.toProfileResponse(user);
    }

    // update thoi gian hoat dong
    @Override
    @Transactional // bat buoc vi la update
    public void updatePreferences(UUID userId, UpdatePreferencesRequest request) {
        // tim user theo UUID duoc lay trong token
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // chi cap nhat nhung field ma user gui len
        if(request.workingHourStart() != null){
            user.setWorkingHourStart(request.workingHourStart());
        }
        if(request.workingHourEnd() != null) {
            user.setWorkingHourEnd(request.workingHourEnd());
        }
        if (request.timezone() != null) {
            user.setTimezone(request.timezone());
        }
        userRepository.save(user);
        log.info("Cập nhật preferences thành công cho user: {}", user.getEmail());

        // Kích hoạt Thread chạy ngầm ghi log
        activityLogService.logUserActivityAsync(userId, "cập nhật Cấu hình Thời gian làm việc");
    }
 }
