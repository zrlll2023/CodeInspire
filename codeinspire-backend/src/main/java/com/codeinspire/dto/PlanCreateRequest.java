package com.codeinspire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PlanCreateRequest {
    @NotBlank(message = "规划标题不能为空")
    private String title;
    private String description;
    private String type;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private String targetGoal;
    private Integer priority;
}
