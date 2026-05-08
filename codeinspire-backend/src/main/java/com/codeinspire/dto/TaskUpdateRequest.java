package com.codeinspire.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private String status;
    private Integer priority;
    private LocalDate dueDate;
    private LocalDate completedAt;
    private String category;
    private String estimatedHours;
}
