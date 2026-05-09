package com.dev.dev_track.dto.request;

import com.dev.dev_track.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    private LocalDate dueDate;
}
