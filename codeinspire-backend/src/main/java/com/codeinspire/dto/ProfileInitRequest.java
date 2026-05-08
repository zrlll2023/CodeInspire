package com.codeinspire.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ProfileInitRequest {
    private String schoolLevel;
    private String educationLevel;
    private String major;
    private String grade;
    private String majorDirection;
    private String targetPosition;
    private String targetCityLevel;
    private String urgencyLevel;
    private Integer weeklyAvailableHours;
    private List<Map<String, Object>> skills;
}
