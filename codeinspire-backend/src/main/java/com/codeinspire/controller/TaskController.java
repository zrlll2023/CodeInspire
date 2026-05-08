package com.codeinspire.controller;

import com.codeinspire.dto.TaskCreateRequest;
import com.codeinspire.dto.TaskUpdateRequest;
import com.codeinspire.entity.Task;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.TaskService;
import com.codeinspire.vo.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ApiResponse<Task> createTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TaskCreateRequest request) {
        Task task = taskService.createTask(userDetails.getId(), request);
        return ApiResponse.success("任务创建成功", task);
    }

    @GetMapping
    public ApiResponse<List<Task>> getTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String status) {
        List<Task> tasks = taskService.getAllUserTasks(userDetails.getId(), status);
        return ApiResponse.success(tasks);
    }

    @GetMapping("/{taskId}")
    public ApiResponse<Task> getTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long taskId) {
        Task task = taskService.getTasksByPlan(null, userDetails.getId()).stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("任务不存在"));
        return ApiResponse.success(task);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<Task> updateTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequest request) {
        Task task = taskService.updateTask(taskId, userDetails.getId(), request);
        return ApiResponse.success("任务更新成功", task);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long taskId) {
        taskService.deleteTask(taskId, userDetails.getId());
        return ApiResponse.success("任务已删除", null);
    }

    @PostMapping("/{taskId}/complete")
    public ApiResponse<Task> completeTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long taskId) {
        Task task = taskService.completeTask(taskId, userDetails.getId());
        return ApiResponse.success("任务已完成", task);
    }

    @GetMapping("/today")
    public ApiResponse<List<Task>> getTodayTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Task> tasks = taskService.getTodayTasks(userDetails.getId());
        return ApiResponse.success(tasks);
    }

    @GetMapping("/overdue")
    public ApiResponse<List<Task>> getOverdueTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Task> tasks = taskService.getOverdueTasks(userDetails.getId());
        return ApiResponse.success(tasks);
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<Task>> getUpcomingTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "7") int days) {
        List<Task> tasks = taskService.getUpcomingTasks(userDetails.getId(), days);
        return ApiResponse.success(tasks);
    }
}
