package com.codeinspire.controller;

import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.VisualizationService;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visualization")
@RequiredArgsConstructor
public class VisualizationController {

    private final VisualizationService visualizationService;
    private final UserProfileService userProfileService;

    @GetMapping("/radar")
    public ApiResponse<?> getSkillRadar(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var data = visualizationService.getSkillRadarChart(userDetails.getId());
        return ApiResponse.success(data);
    }

    @GetMapping("/progress")
    public ApiResponse<?> getLearningProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "30") int days) {
        var data = visualizationService.getLearningProgress(userDetails.getId(), days);
        return ApiResponse.success(data);
    }

    @GetMapping("/skills")
    public ApiResponse<?> getDetailedSkills(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var skills = visualizationService.getDetailedSkillLevels(userDetails.getId());
        return ApiResponse.success(skills);
    }

    @PostMapping("/skills/record")
    public ApiResponse<?> recordSkill(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> request) {
        String category = (String) request.get("category");
        String name = (String) request.get("name");
        int level = (Integer) request.getOrDefault("level", 0);
        String method = (String) request.get("method");

        visualizationService.recordSkillAssessment(userDetails.getId(), category, name, level, method);
        return ApiResponse.success("技能评估记录成功", null);
    }

    @PostMapping("/snapshot")
    public ApiResponse<?> createSnapshot(@AuthenticationPrincipal CustomUserDetails userDetails) {
        visualizationService.createGrowthSnapshot(userDetails.getId());
        return ApiResponse.success("成长快照创建成功", null);
    }

    @GetMapping("/analysis")
    public ApiResponse<?> getProfileAnalysis(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var profile = userProfileService.getProfileByUserId(userDetails.getId());
        var analysis = visualizationService.getProfileAnalysis(userDetails.getId(), profile);
        return ApiResponse.success(analysis);
    }
}
