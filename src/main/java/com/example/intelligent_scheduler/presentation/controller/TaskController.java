package com.example.intelligent_scheduler.presentation.controller;

import com.example.intelligent_scheduler.application.task.dto.TaskRequest;
import com.example.intelligent_scheduler.application.task.dto.TaskResponse;
import com.example.intelligent_scheduler.application.task.service.TaskService;


import com.example.intelligent_scheduler.domain.enums.TaskStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // helper method dung chung de trich xuat userId tu JWT token
    private UUID getUserIdFromToken(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    //api tao task moi (BackLog)
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TaskRequest taskRequest) {
        UUID userId = getUserIdFromToken(jwt);
        TaskResponse taskResponse = taskService.createTask(userId, taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    // 2. API Lấy danh sách Task (Có thể lọc theo status: TODO, IN_PROGRESS, DONE)
    // Ví dụ URL: /api/v1/tasks hoặc /api/v1/tasks?status=TODO
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@AuthenticationPrincipal Jwt jwt, @RequestParam(required = false)TaskStatus status) {
        UUID userId = getUserIdFromToken(jwt);
        log.info("getTasks userId = {}", userId);
        List<TaskResponse> tasks = taskService.getTasks(userId, status);
        return ResponseEntity.ok(tasks);
    }

    // 3. API Cập nhật Task (chỉ được cập nhật task của chính mình)
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@AuthenticationPrincipal Jwt jwt, @PathVariable("taskId") Long taskid, @Valid @RequestBody TaskRequest taskRequest) {
        UUID userId = getUserIdFromToken(jwt);
        log.info("API: User updating Task userId = {})" , userId);
        TaskResponse taskResponse = taskService.updateTask(userId, taskid, taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    //4. Cap nhat trang thai task
    // URL: /api/v1/tasks/12/status?status=IN_PROGRESS, Dung PATCH vi chi cap nhat trang thai, khong can truyen toan bo thong tin task nhu PUT
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateTaskStatus(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Long taskId, @RequestParam TaskStatus status) {
        UUID userId = getUserIdFromToken(jwt);
        log.info("API: User updating Task Status userId = {})" , userId);
        taskService.updateTaskStatus(userId, taskId, status);
        return ResponseEntity.ok("Cập nhật trạng thái Task thành công!");
    }

     //5. API Xóa mem Task (chỉ được xóa task của chính mình)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") Long id) {
        UUID userId = getUserIdFromToken(jwt);
        log.info("API: User deleting Task userId = {})" , userId);
        taskService.deleteTask(userId, id);
        return ResponseEntity.ok("Xóa Task thành công!");
    }
}