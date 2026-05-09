package com.dev.dev_track.repo;

import com.dev.dev_track.entities.TaskEntity;
import com.dev.dev_track.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByUser_UserId(Long userId);

    List<TaskEntity> findByUser_UserIdAndStatus(Long userId, TaskStatus status);

    List<TaskEntity> findByUser_UserIdAndDueDateBetween(Long userId, LocalDate from, LocalDate to);

    List<TaskEntity> findByUser_UserIdAndStatusAndDueDateBetween(
            Long userId, TaskStatus status, LocalDate from, LocalDate to);
}
