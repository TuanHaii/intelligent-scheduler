package com.example.intelligent_scheduler.application.schedule.service.impl;


import com.example.intelligent_scheduler.application.schedule.dto.ScheduleRequest;
import com.example.intelligent_scheduler.application.schedule.dto.ScheduleResponse;
import com.example.intelligent_scheduler.application.schedule.mapper.ScheduleMapper;

import com.example.intelligent_scheduler.application.schedule.service.ScheduleService;
import com.example.intelligent_scheduler.domain.entity.Schedule;
import com.example.intelligent_scheduler.domain.entity.Task;
import com.example.intelligent_scheduler.domain.entity.User;
import com.example.intelligent_scheduler.domain.enums.ScheduleStatus;
import com.example.intelligent_scheduler.domain.enums.TaskStatus;
import com.example.intelligent_scheduler.repository.ScheduleRepository;
import com.example.intelligent_scheduler.repository.TaskRepository;
import com.example.intelligent_scheduler.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
     private final ScheduleRepository scheduleRepository;
     private final UserRepository userRepository;
     private final TaskRepository taskRepository;
     private final ScheduleMapper scheduleMapper;

     // phuong thuc tao 1 lich moi cho user
    @Override
    @Transactional
    public ScheduleResponse createSchedule(UUID userId, ScheduleRequest request ){
        //1. validate logic thoi gian co ban: bat dau phai dien ra truoc ket thuc
        if(request.startTime().isAfter(request.endTime())){
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
        //2. Kiem tra trung lich
        boolean isConflict = scheduleRepository.hasConflict(userId, request.startTime(), request.endTime());
        if(isConflict){
            log.warn("User {} cố gắng xếp lịch bị trùng từ {} đến {}", userId, request.startTime(), request.endTime());
            throw new IllegalArgumentException("Lịch trình bị trùng với một lịch trình đã tồn tại. Vui lòng chọn thời gian khác.");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("Không tìm thấy User để tạo lịch"));

        //3. nếu user muốn gắn lịch này cho công việc cụ thể
        Task task = null;
        if(request.taskId() != null){
            task = taskRepository.findById(request.taskId()).orElseThrow(()->new IllegalArgumentException("Không tìm thấy Task để gắn lịch"));
            //  đảm bảo task này thuộc vào user đang tạo lịch
            if(!task.getUser().getId().equals(userId)){
                throw new IllegalArgumentException("Task không thuộc về User");
            }
        }

        //4. luu sự kiện vào database
        Schedule schedule = Schedule.builder()
                .user(user)
                .task(task)
                .title(request.title())
                .status(ScheduleStatus.PLANNED) // mac dinh tao lich moi se la planned
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();
        Schedule savedSchedule = scheduleRepository.save(schedule);
        log.info("User {} đã tạo lịch mới: {} từ {} đến {}", userId, savedSchedule.getId(), request.startTime(), request.endTime());
        return scheduleMapper.toScheduleResponse(savedSchedule);
    }

    // get schedule cua user
    @Override
    public List<ScheduleResponse> getSchedules(UUID userId, OffsetDateTime start, OffsetDateTime end){
        if(start.isAfter(end)){
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
        List<Schedule> schedules = scheduleRepository.findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(userId, start, end);
        return schedules.stream().map(scheduleMapper::toScheduleResponse).toList();
    }
}
