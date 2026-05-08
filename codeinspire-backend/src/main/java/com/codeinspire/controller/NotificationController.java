package com.codeinspire.controller;

import com.codeinspire.notification.NotificationService;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<?>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean isRead) {
        var notifications = notificationService.getUserNotifications(userDetails.getId(), isRead);
        return ApiResponse.success(notifications);
    }

    @GetMapping("/unread-count")
    public ApiResponse<?> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long count = notificationService.getUnreadCount(userDetails.getId());
        return ApiResponse.success(Map.of("unreadCount", count));
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<Void> markAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId, userDetails.getId());
        return ApiResponse.success(null);
    }

    @PutMapping("/read-all")
    public ApiResponse<Void> markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> deleteNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId, userDetails.getId());
        return ApiResponse.success(null);
    }

    @GetMapping("/settings")
    public ApiResponse<?> getSettings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var settings = notificationService.getOrCreateSettings(userDetails.getId());
        return ApiResponse.success(settings);
    }

    @PutMapping("/settings")
    public ApiResponse<?> updateSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> updates) {
        var settings = notificationService.updateSettings(userDetails.getId(), updates);
        return ApiResponse.success("设置更新成功", settings);
    }
}
