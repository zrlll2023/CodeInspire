package com.codeinspire.controller;

import com.codeinspire.dto.ProfileInitRequest;
import com.codeinspire.dto.UserProfileFullRequest;
import com.codeinspire.entity.UserProfile;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.InfoCollectionService;
import com.codeinspire.service.UserProfileService;
import com.codeinspire.vo.ApiResponse;
import com.codeinspire.vo.UserProfileVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;
    private final InfoCollectionService infoCollectionService;

    @GetMapping
    public ApiResponse<UserProfile> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        return ApiResponse.success(profile);
    }

    @GetMapping("/full")
    public ApiResponse<UserProfileVO> getFullProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        UserProfileVO vo = convertToVO(profile);
        return ApiResponse.success(vo);
    }

    @PostMapping("/init")
    public ApiResponse<UserProfile> initProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProfileInitRequest request) {
        UserProfile profile = userProfileService.initProfile(userDetails.getId(), request);
        return ApiResponse.success("画像初始化成功", profile);
    }

    @PutMapping
    public ApiResponse<UserProfile> updateProfile(
            @AuthenticationPrincipal CustomUserDetails UserDetails,
            @Valid @RequestBody ProfileInitRequest request) {
        UserProfile profile = userProfileService.updateProfile(UserDetails.getId(), request);
        return ApiResponse.success("画像更新成功", profile);
    }

    @PutMapping("/full")
    public ApiResponse<UserProfile> updateFullProfile(
            @AuthenticationPrincipal CustomUserDetails UserDetails,
            @RequestBody UserProfileFullRequest request) {
        UserProfile profile = userProfileService.updateFullProfile(UserDetails.getId(), request);
        return ApiResponse.success("完整画像更新成功", profile);
    }

    @GetMapping("/completeness")
    public ApiResponse<?> getCompleteness(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        double score = infoCollectionService.calculateProfileCompleteness(profile);
        return ApiResponse.success(Map.of("completenessScore", score));
    }

    @GetMapping("/suggestions")
    public ApiResponse<?> getSuggestions(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        Map<String, Object> profileMap = profileToMap(profile);
        var result = infoCollectionService.getPriorityQuestions(profileMap, "general", 3);
        return ApiResponse.success(Map.of("suggestions", result));
    }

    private UserProfileVO convertToVO(UserProfile profile) {
        if (profile == null) return null;

        double completeness = infoCollectionService.calculateProfileCompleteness(profile);

        UserProfileVO vo = UserProfileVO.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .education(UserProfileVO.ProfileEducation.builder()
                        .schoolLevel(profile.getSchoolLevel())
                        .schoolType(profile.getSchoolType())
                        .educationLevel(profile.getEducationLevel())
                        .major(profile.getMajor())
                        .grade(profile.getGrade())
                        .build())
                .tech(UserProfileVO.ProfileTech.builder()
                        .skills(parseJsonList(profile.getSkills()))
                        .projects(parseJsonList(profile.getProjects()))
                        .certifications(parseJsonStringList(profile.getCertifications()))
                        .build())
                .time(UserProfileVO.ProfileTime.builder()
                        .grade(profile.getGrade())
                        .urgencyLevel(profile.getUrgencyLevel())
                        .weeklyAvailableHours(profile.getWeeklyAvailableHours())
                        .courseworkPressure(profile.getCourseworkPressure())
                        .build())
                .location(UserProfileVO.ProfileLocation.builder()
                        .targetCityLevel(profile.getTargetCityLevel())
                        .hometownConsideration(profile.getHometownConsideration())
                        .industryPreference(profile.getIndustryPreference())
                        .build())
                .economic(UserProfileVO.ProfileEconomic.builder()
                        .paymentWillingness(profile.getPaymentWillingness())
                        .computerConfig(profile.getComputerConfig())
                        .selfLearningAbility(profile.getSelfLearningAbility())
                        .economicPressure(profile.getEconomicPressure())
                        .build())
                .career(UserProfileVO.ProfileCareer.builder()
                        .currentStatus(profile.getCurrentStatus())
                        .majorDirection(profile.getMajorDirection())
                        .targetPosition(profile.getTargetPosition())
                        .targetCompany(profile.getTargetCompany())
                        .expectedSalary(profile.getExpectedSalary())
                        .build())
                .completenessScore(completeness)
                .build();

        return vo;
    }

    private java.util.List<java.util.Map<String, Object>> parseJsonList(String json) {
        if (json == null || json.isBlank()) return new java.util.ArrayList<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<>() {});
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    private java.util.List<String> parseJsonStringList(String json) {
        if (json == null || json.isBlank()) return new java.util.ArrayList<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<>() {});
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    private Map<String, Object> profileToMap(UserProfile profile) {
        Map<String, Object> map = new java.util.HashMap<>();
        if (profile != null) {
            map.put("schoolLevel", profile.getSchoolLevel());
            map.put("grade", profile.getGrade());
            map.put("targetPosition", profile.getTargetPosition());
            map.put("targetCityLevel", profile.getTargetCityLevel());
            map.put("urgencyLevel", profile.getUrgencyLevel());
            map.put("weeklyAvailableHours", profile.getWeeklyAvailableHours());
            map.put("majorDirection", profile.getMajorDirection());
            map.put("skills", profile.getSkills());
            map.put("projects", profile.getProjects());
        }
        return map;
    }
}
