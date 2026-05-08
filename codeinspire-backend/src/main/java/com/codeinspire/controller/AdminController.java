package com.codeinspire.controller;

import com.codeinspire.feedback.FeedbackService;
import com.codeinspire.service.AdminStatsService;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminStatsService adminStatsService;
    private final FeedbackService feedbackService;

    @GetMapping("/stats/overview")
    public ApiResponse<?> getOverviewStats() {
        var stats = adminStatsService.getOverviewStats();
        return ApiResponse.success(stats);
    }

    @GetMapping("/stats/users")
    public ApiResponse<?> getUserStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var stats = adminStatsService.getUserStats(startDate, endDate);
        return ApiResponse.success(stats);
    }

    @GetMapping("/stats/ai-usage")
    public ApiResponse<?> getAiUsageStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var stats = adminStatsService.getAiUsageStats(startDate, endDate);
        return ApiResponse.success(stats);
    }

    @GetMapping("/stats/content-quality")
    public ApiResponse<?> getContentQualityStats() {
        var stats = adminStatsService.getContentQualityStats();
        return ApiResponse.success(stats);
    }

    @GetMapping("/stats/dau")
    public ApiResponse<List<?>> getDailyActiveUsers(@RequestParam(defaultValue = "30") int days) {
        var data = adminStatsService.getDailyActiveUsers(days);
        return ApiResponse.success(data);
    }

    @GetMapping("/feedbacks")
    public ApiResponse<List<?>> getFeedbacks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        var feedbacks = feedbackService.getAllFeedbacks(status, page, size);
        return ApiResponse.success(feedbacks);
    }

    @GetMapping("/feedbacks/stats")
    public ApiResponse<?> getFeedbackStats() {
        var stats = feedbackService.getFeedbackStats();
        return ApiResponse.success(stats);
    }

    @PutMapping("/feedbacks/{feedbackId}/reply")
    public ApiResponse<?> replyToFeedback(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, String> request) {
        feedbackService.addAdminReply(feedbackId, request.get("reply"));
        return ApiResponse.success("回复成功", null);
    }

    @PutMapping("/feedbacks/{feedbackId}/status")
    public ApiResponse<?> updateFeedbackStatus(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, String> request) {
        feedbackService.updateStatus(feedbackId, request.get("status"));
        return ApiResponse.success("状态更新成功", null);
    }
}
