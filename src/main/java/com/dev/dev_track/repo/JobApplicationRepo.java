package com.dev.dev_track.repo;

import com.dev.dev_track.entities.JobApplicationEntity;
import com.dev.dev_track.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplicationEntity, Long> {

    List<JobApplicationEntity> findByUser_UserId(Long userId);

    List<JobApplicationEntity> findByUser_UserIdAndStatus(Long userId, ApplicationStatus status);

    List<JobApplicationEntity> findByUser_UserIdAndCompanyNameContainingIgnoreCase(
            Long userId, String companyName);
}
