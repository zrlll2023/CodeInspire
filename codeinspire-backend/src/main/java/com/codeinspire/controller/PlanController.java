package com.codeinspire.controller;

import com.codeinspire.dto.PlanCreateRequest;
import com.codeinspire.dto.TaskCreateRequest;
import com.codeinspire.dto.TaskUpdateRequest;
import com.codeinspire.entity.Plan;
import com.codeinspire.entity.Task;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.PlanService;
import com.codeinspire.service.TaskService;
import com.codeinspire.vo.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final TaskService taskService;

    @PostMapping
    public ApiResponse<Plan> createPlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PlanCreateRequest request) {
        Plan plan = planService.createPlan(userDetails.getId(), request);
        return ApiResponse.success("规划创建成功", plan);
    }

    @GetMapping
    public ApiResponse<List<Plan>> getPlans(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String status) {
        List<Plan> plans = planService.getUserPlans(userDetails.getId(), status);
        return ApiResponse.success(plans);
    }

    @GetMapping("/{planId}")
    public ApiResponse<Plan> getPlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId) {
        Plan plan = planService.getPlanById(planId, userDetails.getId());
        return ApiResponse.success(plan);
    }

    @PutMapping("/{planId}")
    public ApiResponse<Plan> updatePlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId,
            @RequestBody PlanCreateRequest request) {
        Plan plan = planService.updatePlan(planId, userDetails.getId(), request);
        return ApiResponse.success("规划更新成功", plan);
    }

    @DeleteMapping("/{planId}")
    public ApiResponse<Void> deletePlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId) {
        planService.deletePlan(planId, userDetails.getId());
        return ApiResponse.success("规划已归档", null);
    }

    @PostMapping("/{planId}/complete")
    public ApiResponse<Void> completePlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId) {
        planService.completePlan(planId, userDetails.getId());
        return ApiResponse.success("规划已完成", null);
    }

    @PostMapping("/{planId}/tasks")
    public ApiResponse<Task> addTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId,
            @Valid @RequestBody TaskCreateRequest request) {
        request.setPlanId(planId);
        Task task = taskService.createTask(userDetails.getId(), request);
        return ApiResponse.success("任务添加成功", task);
    }

    @GetMapping("/{planId}/tasks")
    public ApiResponse<List<Task>> getTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long planId) {
        List<Task> tasks = taskService.getTasksByPlan(planId, userDetails.getId());
        return ApiResponse.success(tasks);
    }
}
