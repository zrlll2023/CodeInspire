package com.codeinspire.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVO {
    private Long id;
    private Long userId;
    private ProfileEducation education;
    private ProfileTech tech;
    private ProfileTime time;
    private ProfileLocation location;
    private ProfileEconomic economic;
    private ProfileCareer career;
    private Double completenessScore;
    private List<String> missingFields;
    private String profileSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEducation {
        private String schoolLevel;
        private String schoolType;
        private String educationLevel;
        private String major;
        private String grade;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileTech {
        private List<Map<String, Object>> skills;
        private List<Map<String, Object>> projects;
        private List<String> certifications;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileTime {
        private String grade;
        private String urgencyLevel;
        private Integer weeklyAvailableHours;
        private String courseworkPressure;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileLocation {
        private String targetCityLevel;
        private String hometownConsideration;
        private String industryPreference;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEconomic {
        private String paymentWillingness;
        private String computerConfig;
        private String selfLearningAbility;
        private String economicPressure;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileCareer {
        private String currentStatus;
        private String majorDirection;
        private String targetPosition;
        private String targetCompany;
        private String expectedSalary;
    }
}
