package com.example.intelligent_scheduler.repository;

import com.example.intelligent_scheduler.domain.entity.Task;
import com.example.intelligent_scheduler.domain.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(UUID id);
    List<Task> findByUserIdAndStatus(UUID userId, TaskStatus status);
}
