package com.codeinspire.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_profiles")
public class UserProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
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
    private String skills;
    private String projects;
    private String certifications;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
