package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.StartFocusSessionRequest;
import com.dev.dev_track.dto.request.StopFocusSessionRequest;
import com.dev.dev_track.dto.response.FocusSessionResponse;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.service.FocusSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/focus-sessions")
@RequiredArgsConstructor
@Tag(name = "Focus Session Controller", description = "Productivity focus session tracking")
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<FocusSessionResponse>> start(
            @RequestBody StartFocusSessionRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(focusSessionService.startSession(getCurrentUserId(auth), request));
    }

    @PatchMapping("/stop")
    public ResponseEntity<ApiResponse<FocusSessionResponse>> stop(
            @RequestBody StopFocusSessionRequest request, Authentication auth) {
        return ResponseEntity.ok(focusSessionService.stopSession(getCurrentUserId(auth), request));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<FocusSessionResponse>> active(Authentication auth) {
        return ResponseEntity.ok(focusSessionService.getActiveSession(getCurrentUserId(auth)));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<FocusSessionResponse>>> history(Authentication auth) {
        return ResponseEntity.ok(focusSessionService.getSessionHistory(getCurrentUserId(auth)));
    }

    private Long getCurrentUserId(Authentication auth) {
        return ((UserEntity) auth.getPrincipal()).getUserId();
    }
}
