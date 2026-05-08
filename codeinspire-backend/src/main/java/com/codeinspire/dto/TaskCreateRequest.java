package com.codeinspire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskCreateRequest {
    @NotBlank(message = "任务标题不能为空")
    private String title;
    private Long planId;
    private String description;
    private Integer priority;
    private LocalDate dueDate;
    private String category;
    private String estimatedHours;
    private Integer sortOrder;
}
