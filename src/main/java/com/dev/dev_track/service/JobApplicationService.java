package com.dev.dev_track.service;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.CreateJobApplicationRequest;
import com.dev.dev_track.dto.request.UpdateJobApplicationRequest;
import com.dev.dev_track.dto.request.UpdateApplicationStatusRequest;
import com.dev.dev_track.dto.response.JobApplicationResponse;
import com.dev.dev_track.entities.JobApplicationEntity;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.enums.ApplicationStatus;
import com.dev.dev_track.exception.ResourceNotFoundException;
import com.dev.dev_track.repo.JobApplicationRepo;
import com.dev.dev_track.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepo jobApplicationRepo;
    private final UserRepo userRepo;

    public ApiResponse<JobApplicationResponse> createApplication(
            Long userId, CreateJobApplicationRequest request) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobApplicationEntity entity = JobApplicationEntity.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobUrl(request.getJobUrl())
                .status(request.getStatus())
                .appliedDate(request.getAppliedDate())
                .notes(request.getNotes())
                .build();

        return ApiResponse.ok("Application created", toResponse(jobApplicationRepo.save(entity)));
    }

    public ApiResponse<JobApplicationResponse> getApplicationById(Long userId, Long applicationId) {
        return ApiResponse.ok("Application found", toResponse(findOwnedApplication(userId, applicationId)));
    }

    public ApiResponse<List<JobApplicationResponse>> getAllByUser(Long userId,
                                                                   ApplicationStatus status) {
        List<JobApplicationEntity> applications = status != null
                ? jobApplicationRepo.findByUser_UserIdAndStatus(userId, status)
                : jobApplicationRepo.findByUser_UserId(userId);

        return ApiResponse.ok("Applications retrieved",
                applications.stream().map(this::toResponse).toList());
    }

    public ApiResponse<JobApplicationResponse> updateApplication(
            Long userId, Long applicationId, UpdateJobApplicationRequest request) {
        JobApplicationEntity entity = findOwnedApplication(userId, applicationId);
        entity.setCompanyName(request.getCompanyName());
        entity.setJobTitle(request.getJobTitle());
        entity.setJobUrl(request.getJobUrl());
        entity.setStatus(request.getStatus());
        entity.setAppliedDate(request.getAppliedDate());
        entity.setNotes(request.getNotes());
        return ApiResponse.ok("Application updated", toResponse(jobApplicationRepo.save(entity)));
    }

    public ApiResponse<JobApplicationResponse> updateStatus(
            Long userId, Long applicationId, UpdateApplicationStatusRequest request) {
        JobApplicationEntity entity = findOwnedApplication(userId, applicationId);
        entity.setStatus(request.getStatus());
        return ApiResponse.ok("Status updated", toResponse(jobApplicationRepo.save(entity)));
    }

    public ApiResponse<Void> deleteApplication(Long userId, Long applicationId) {
        jobApplicationRepo.delete(findOwnedApplication(userId, applicationId));
        return ApiResponse.ok("Application deleted");
    }

    private JobApplicationEntity findOwnedApplication(Long userId, Long applicationId) {
        JobApplicationEntity entity = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!entity.getUser().getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found");
        }
        return entity;
    }

    private JobApplicationResponse toResponse(JobApplicationEntity entity) {
        return JobApplicationResponse.builder()
                .applicationId(entity.getApplicationId())
                .companyName(entity.getCompanyName())
                .jobTitle(entity.getJobTitle())
                .jobUrl(entity.getJobUrl())
                .status(entity.getStatus())
                .appliedDate(entity.getAppliedDate())
                .notes(entity.getNotes())
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }
}
