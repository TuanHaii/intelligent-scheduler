package com.example.intelligent_scheduler.application.task.service.impl;

import com.example.intelligent_scheduler.application.task.dto.TaskRequest;
import com.example.intelligent_scheduler.application.task.dto.TaskResponse;
import com.example.intelligent_scheduler.application.task.mapper.TaskMapper;
import com.example.intelligent_scheduler.application.task.service.TaskService;
import com.example.intelligent_scheduler.domain.entity.Task;
import com.example.intelligent_scheduler.domain.entity.User;
import com.example.intelligent_scheduler.domain.enums.TaskStatus;
import com.example.intelligent_scheduler.repository.TaskRepository;
import com.example.intelligent_scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    // taoj task mới
    @Override
    @Transactional
    public TaskResponse createTask(UUID userId, TaskRequest request){
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("Không tìm thấy User để tạo công việc"));
        Task task = Task.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(TaskStatus.TODO) // mac dinh tao task se la todo
                .estimatedDuration(request.estimatedDurationMinutes())
                .deadline(request.deadline())
                .build();
        Task savedTask = taskRepository.save(task);
        log.info("User {} đã tạo Task mới: {}", userId, savedTask.getId());
        return taskMapper.toTaskResponse(savedTask);
    }

    // Get list task
    public List<TaskResponse> getTasks(UUID userId, TaskStatus status) {
        List<Task> tasks;
        // loc theo trang thai
        if(status != null) {
            tasks = taskRepository.findByUserIdAndStatus(userId, status);
        }
        else{
            // lay cac task chua bi xoa
            tasks = taskRepository.findByUserId(userId);
        }
        return tasks.stream().map(taskMapper::toTaskResponse).collect(Collectors.toList());
    }

    // update task
    @Override
    @Transactional
    public TaskResponse updateTask(UUID userId, Long taskId, TaskRequest request){
        Task task = getValidTaskForUser(userId, taskId);

        // cap nhat thong tin task
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setEstimatedDuration(request.estimatedDurationMinutes());
        task.setDeadline(request.deadline());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponse(savedTask);
    }

    // upodate task status
    @Override
    @Transactional
    public void updateTaskStatus(UUID userId, Long taskId, TaskStatus status){
        Task task =  getValidTaskForUser(userId, taskId);
        task.setStatus(status);
        taskRepository.save(task);
    }

    // del task
    @Override
    @Transactional
    public void deleteTask(UUID userId, Long taskId) {
        Task task = getValidTaskForUser(userId, taskId);
        task.setDeletedAt(OffsetDateTime.now());
        taskRepository.save(task);
    }

    // kiểm tra task có tồn tại hay không và có thuộc về user đó hay không
    private Task getValidTaskForUser(UUID userId, Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task không tìm thấy hoặc không thuộc về bạn"));
        if(!task.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Task không tìm thấy hoặc không thuộc về bạn");
        }
        if (task.getDeletedAt() != null) {
            throw new IllegalArgumentException("Công việc này đã bị xóa");
        }

        return task;
    }
}
