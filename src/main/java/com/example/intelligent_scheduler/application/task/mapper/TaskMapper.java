package com.example.intelligent_scheduler.application.task.mapper;

import com.example.intelligent_scheduler.application.task.dto.TaskResponse;
import com.example.intelligent_scheduler.domain.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponse toTaskResponse(Task task) {
        if(task == null) return null;

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getEstimatedDuration(),
                task.getDeadline(),
                task.getCreatedAt()
        );
    }
}
