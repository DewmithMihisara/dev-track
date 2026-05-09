package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.CreateTaskRequest;
import com.dev.dev_track.dto.request.UpdateTaskRequest;
import com.dev.dev_track.dto.request.UpdateTaskStatusRequest;
import com.dev.dev_track.dto.response.TaskResponse;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.enums.TaskStatus;
import com.dev.dev_track.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Task CRUD operations")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> create(
            @Valid @RequestBody CreateTaskRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(getCurrentUserId(auth), request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAll(Authentication auth) {
        return ResponseEntity.ok(taskService.getAllTasksByUser(getCurrentUserId(auth)));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getById(
            @PathVariable Long taskId, Authentication auth) {
        return ResponseEntity.ok(taskService.getTaskById(getCurrentUserId(auth), taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication auth) {
        return ResponseEntity.ok(taskService.updateTask(getCurrentUserId(auth), taskId, request));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                taskService.updateTaskStatus(getCurrentUserId(auth), taskId, request));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> filter(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication auth) {
        return ResponseEntity.ok(
                taskService.filterTasks(getCurrentUserId(auth), status, from, to));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long taskId, Authentication auth) {
        return ResponseEntity.ok(taskService.deleteTask(getCurrentUserId(auth), taskId));
    }

    private Long getCurrentUserId(Authentication auth) {
        return ((UserEntity) auth.getPrincipal()).getUserId();
    }
}
