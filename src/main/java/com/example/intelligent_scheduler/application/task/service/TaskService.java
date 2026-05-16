package com.example.intelligent_scheduler.application.task.service;

import com.example.intelligent_scheduler.application.task.dto.TaskRequest;
import com.example.intelligent_scheduler.application.task.dto.TaskResponse;
import com.example.intelligent_scheduler.domain.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskService{
    List<TaskResponse> getTasks(UUID userId, TaskStatus status);
    TaskResponse createTask(UUID userId, TaskRequest request);
    TaskResponse updateTask(UUID userId, Long taskId, TaskRequest request);
    void deleteTask(UUID userId, Long taskId);
    void updateTaskStatus(UUID userId, Long taskId, TaskStatus status);
}