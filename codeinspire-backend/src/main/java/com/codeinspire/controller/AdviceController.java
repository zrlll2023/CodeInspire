package com.codeinspire.controller;

import com.codeinspire.dto.UserProfileFullRequest;
import com.codeinspire.entity.UserProfile;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.*;
import com.codeinspire.vo.ApiResponse;
import com.codeinspire.vo.UserProfileVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advice")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceEngineService adviceEngineService;
    private final InfoCollectionService infoCollectionService;
    private final UserProfileService userProfileService;
    private final VisualizationService visualizationService;

    @PostMapping("/consult")
    public ApiResponse<?> consult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody com.codeinspire.service.AdviceEngineService.AdviceRequest request) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        request.setUserProfile(profile);
        var response = adviceEngineService.generateAdvice(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/scenarios")
    public ApiResponse<?> getScenarios() {
        List<Map<String, String>> scenarios = List.of(
                Map.of("key", "career_advice", "name", "求职建议", "description", "个性化求职策略和准备计划"),
                Map.of("key", "tech_learning", "name", "技术学习", "description", "学习路线规划和技能提升"),
                Map.of("key", "interview_prep", "name", "面试准备", "description", "面试题库和模拟练习"),
                Map.of("key", "general", "name", "通用咨询", "description", "综合问题解答")
        );
        return ApiResponse.success(scenarios);
    }

    @PostMapping("/collect")
    public ApiResponse<?> collectMissingInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> currentProfile) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        Map<String, Object> profileMap = profileToMap(profile);
        if (currentProfile != null) {
            profileMap.putAll(currentProfile);
        }

        String scene = (String) currentProfile.getOrDefault("scene", "general");
        var result = infoCollectionService.checkCompleteness(profileMap, scene);
        return ApiResponse.success(result);
    }
}
