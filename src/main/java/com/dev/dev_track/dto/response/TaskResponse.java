package com.dev.dev_track.dto.response;

import com.dev.dev_track.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private Long taskId;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
