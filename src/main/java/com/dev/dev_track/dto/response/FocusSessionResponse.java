package com.dev.dev_track.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FocusSessionResponse {

    private Long sessionId;
    private Long taskId;
    private String taskTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String notes;
    private boolean active;
}
