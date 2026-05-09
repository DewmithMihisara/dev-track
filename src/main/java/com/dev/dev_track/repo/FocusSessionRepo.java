package com.dev.dev_track.repo;

import com.dev.dev_track.entities.FocusSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FocusSessionRepo extends JpaRepository<FocusSessionEntity, Long> {

    List<FocusSessionEntity> findByUser_UserIdOrderByStartTimeDesc(Long userId);

    Optional<FocusSessionEntity> findByUser_UserIdAndEndTimeIsNull(Long userId);

    List<FocusSessionEntity> findByUser_UserIdAndStartTimeBetween(
            Long userId, LocalDateTime from, LocalDateTime to);
}
