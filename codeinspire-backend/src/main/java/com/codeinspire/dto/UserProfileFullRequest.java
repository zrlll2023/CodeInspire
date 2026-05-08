package com.codeinspire.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class UserProfileFullRequest {
    private String schoolLevel;
    private String schoolType;
    private String educationLevel;
    private String major;
    private String grade;
    private String urgencyLevel;
    private Integer weeklyAvailableHours;
    private String courseworkPressure;
    private String targetCityLevel;
    private String hometownConsideration;
    private String industryPreference;
    private String paymentWillingness;
    private String computerConfig;
    private String selfLearningAbility;
    private String economicPressure;
    private String currentStatus;
    private String majorDirection;
    private String targetPosition;
    private String targetCompany;
    private String expectedSalary;
    private List<Map<String, Object>> skills;
    private List<Map<String, Object>> projects;
    private List<String> certifications;
}
