package com.dev.dev_track.service;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.StartFocusSessionRequest;
import com.dev.dev_track.dto.request.StopFocusSessionRequest;
import com.dev.dev_track.dto.response.FocusSessionResponse;
import com.dev.dev_track.entities.FocusSessionEntity;
import com.dev.dev_track.entities.TaskEntity;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.exception.DuplicateResourceException;
import com.dev.dev_track.exception.ResourceNotFoundException;
import com.dev.dev_track.repo.FocusSessionRepo;
import com.dev.dev_track.repo.TaskRepo;
import com.dev.dev_track.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusSessionService {

    private final FocusSessionRepo focusSessionRepo;
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;

    public ApiResponse<FocusSessionResponse> startSession(Long userId,
                                                           StartFocusSessionRequest request) {
        focusSessionRepo.findByUser_UserIdAndEndTimeIsNull(userId).ifPresent(s -> {
            throw new DuplicateResourceException("A focus session is already active");
        });

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskEntity task = null;
        if (request.getTaskId() != null) {
            task = taskRepo.findById(request.getTaskId())
                    .filter(t -> t.getUser().getUserId().equals(userId))
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        }

        FocusSessionEntity session = FocusSessionEntity.builder()
                .user(user)
                .task(task)
                .startTime(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        return ApiResponse.ok("Focus session started", toResponse(focusSessionRepo.save(session)));
    }

    public ApiResponse<FocusSessionResponse> stopSession(Long userId,
                                                          StopFocusSessionRequest request) {
        FocusSessionEntity session = focusSessionRepo
                .findByUser_UserIdAndEndTimeIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No active focus session found"));

        session.setEndTime(LocalDateTime.now());
        session.setDurationMinutes(
                (int) ChronoUnit.MINUTES.between(session.getStartTime(), session.getEndTime()));

        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            session.setNotes(request.getNotes());
        }

        return ApiResponse.ok("Focus session stopped", toResponse(focusSessionRepo.save(session)));
    }

    public ApiResponse<FocusSessionResponse> getActiveSession(Long userId) {
        return focusSessionRepo.findByUser_UserIdAndEndTimeIsNull(userId)
                .map(s -> ApiResponse.ok("Active session found", toResponse(s)))
                .orElse(ApiResponse.ok("No active session", null));
    }

    public ApiResponse<List<FocusSessionResponse>> getSessionHistory(Long userId) {
        List<FocusSessionResponse> history = focusSessionRepo
                .findByUser_UserIdOrderByStartTimeDesc(userId)
                .stream().map(this::toResponse).toList();
        return ApiResponse.ok("Session history retrieved", history);
    }

    private FocusSessionResponse toResponse(FocusSessionEntity session) {
        return FocusSessionResponse.builder()
                .sessionId(session.getSessionId())
                .taskId(session.getTask() != null ? session.getTask().getTaskId() : null)
                .taskTitle(session.getTask() != null ? session.getTask().getTitle() : null)
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .durationMinutes(session.getDurationMinutes())
                .notes(session.getNotes())
                .active(session.getEndTime() == null)
                .build();
    }
}
