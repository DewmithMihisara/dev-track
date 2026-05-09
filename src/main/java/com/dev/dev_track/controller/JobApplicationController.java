package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.CreateJobApplicationRequest;
import com.dev.dev_track.dto.request.UpdateApplicationStatusRequest;
import com.dev.dev_track.dto.request.UpdateJobApplicationRequest;
import com.dev.dev_track.dto.response.JobApplicationResponse;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.enums.ApplicationStatus;
import com.dev.dev_track.service.JobApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
@Tag(name = "Job Application Controller", description = "Job application tracking operations")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobApplicationResponse>> create(
            @Valid @RequestBody CreateJobApplicationRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobApplicationService.createApplication(getCurrentUserId(auth), request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getAll(
            @RequestParam(required = false) ApplicationStatus status, Authentication auth) {
        return ResponseEntity.ok(
                jobApplicationService.getAllByUser(getCurrentUserId(auth), status));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getById(
            @PathVariable Long applicationId, Authentication auth) {
        return ResponseEntity.ok(
                jobApplicationService.getApplicationById(getCurrentUserId(auth), applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> update(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateJobApplicationRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                jobApplicationService.updateApplication(getCurrentUserId(auth), applicationId, request));
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                jobApplicationService.updateStatus(getCurrentUserId(auth), applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long applicationId, Authentication auth) {
        return ResponseEntity.ok(
                jobApplicationService.deleteApplication(getCurrentUserId(auth), applicationId));
    }

    private Long getCurrentUserId(Authentication auth) {
        return ((UserEntity) auth.getPrincipal()).getUserId();
    }
}
