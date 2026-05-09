package com.dev.dev_track.service;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.CreateTaskRequest;
import com.dev.dev_track.dto.request.UpdateTaskRequest;
import com.dev.dev_track.dto.request.UpdateTaskStatusRequest;
import com.dev.dev_track.dto.response.TaskResponse;
import com.dev.dev_track.entities.TaskEntity;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.enums.TaskStatus;
import com.dev.dev_track.exception.ResourceNotFoundException;
import com.dev.dev_track.repo.TaskRepo;
import com.dev.dev_track.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    public ApiResponse<TaskResponse> createTask(Long userId, CreateTaskRequest request) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskEntity task = TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .user(user)
                .build();

        return ApiResponse.ok("Task created", toResponse(taskRepo.save(task)));
    }

    public ApiResponse<TaskResponse> getTaskById(Long userId, Long taskId) {
        return ApiResponse.ok("Task found", toResponse(findOwnedTask(userId, taskId)));
    }

    public ApiResponse<List<TaskResponse>> getAllTasksByUser(Long userId) {
        List<TaskResponse> tasks = taskRepo.findByUser_UserId(userId)
                .stream().map(this::toResponse).toList();
        return ApiResponse.ok("Tasks retrieved", tasks);
    }

    public ApiResponse<TaskResponse> updateTask(Long userId, Long taskId, UpdateTaskRequest request) {
        TaskEntity task = findOwnedTask(userId, taskId);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        if (request.getStatus() == TaskStatus.DONE && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
        } else if (request.getStatus() != TaskStatus.DONE) {
            task.setCompletedAt(null);
        }
        return ApiResponse.ok("Task updated", toResponse(taskRepo.save(task)));
    }

    public ApiResponse<TaskResponse> updateTaskStatus(Long userId, Long taskId,
                                                       UpdateTaskStatusRequest request) {
        TaskEntity task = findOwnedTask(userId, taskId);
        task.setStatus(request.getStatus());
        if (request.getStatus() == TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        return ApiResponse.ok("Task status updated", toResponse(taskRepo.save(task)));
    }

    public ApiResponse<Void> deleteTask(Long userId, Long taskId) {
        taskRepo.delete(findOwnedTask(userId, taskId));
        return ApiResponse.ok("Task deleted");
    }

    public ApiResponse<List<TaskResponse>> filterTasks(Long userId, TaskStatus status,
                                                        LocalDate from, LocalDate to) {
        List<TaskEntity> tasks;
        if (status != null && from != null && to != null) {
            tasks = taskRepo.findByUser_UserIdAndStatusAndDueDateBetween(userId, status, from, to);
        } else if (status != null) {
            tasks = taskRepo.findByUser_UserIdAndStatus(userId, status);
        } else if (from != null && to != null) {
            tasks = taskRepo.findByUser_UserIdAndDueDateBetween(userId, from, to);
        } else {
            tasks = taskRepo.findByUser_UserId(userId);
        }
        return ApiResponse.ok("Tasks filtered", tasks.stream().map(this::toResponse).toList());
    }

    private TaskEntity findOwnedTask(Long userId, Long taskId) {
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (!task.getUser().getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        return task;
    }

    private TaskResponse toResponse(TaskEntity task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .createAt(task.getCreateAt())
                .updateAt(task.getUpdateAt())
                .build();
    }
}
