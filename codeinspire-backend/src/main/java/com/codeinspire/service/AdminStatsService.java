package com.codeinspire.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.entity.AiCallLog;
import com.codeinspire.entity.Notification;
import com.codeinspire.entity.User;
import com.codeinspire.entity.UserFeedback;
import com.codeinspire.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;
    private final AiCallLogRepository aiCallLogRepository;
    private final UserFeedbackRepository userFeedbackRepository;
    private final NotificationRepository notificationRepository;

    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long totalUsers = userRepository.selectCount(null);
        stats.put("totalUsers", totalUsers);

        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        long newUsersThisWeek = userRepository.selectCount(
                new LambdaQueryWrapper<User>().ge(User::getCreatedAt, weekAgo)
        );
        stats.put("newUsersThisWeek", newUsersThisWeek);

        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);
        long newUsersThisMonth = userRepository.selectCount(
                new LambdaQueryWrapper<User>().ge(User::getCreatedAt, monthAgo)
        );
        stats.put("newUsersThisMonth", newUsersThisMonth);

        long totalAiCalls = aiCallLogRepository.selectCount(null);
        stats.put("totalAiCalls", totalAiCalls);

        long successAiCalls = aiCallLogRepository.selectCount(
                new LambdaQueryWrapper<AiCallLog>().eq(AiCallLog::getStatus, "success")
        );
        stats.put("successAiCalls", successAiCalls);
        stats.put("aiSuccessRate", totalAiCalls > 0 ? Math.round((double) successAiCalls / totalAiCalls * 10000) / 100.0 : 0);

        long totalFeedbacks = userFeedbackRepository.selectCount(null);
        stats.put("totalFeedbacks", totalFeedbacks);

        double avgRating = getAverageRating();
        stats.put("averageUserRating", Math.round(avgRating * 100) / 100.0);

        return stats;
    }

    public Map<String, Object> getUserStats(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new LinkedHashMap<>();

        List<User> users = userRepository.selectList(
                new LambdaQueryWrapper<User>()
                        .ge(User::getCreatedAt, startDate.atStartOfDay())
                        .le(User::getCreatedAt, endDate.atTime(LocalTime.MAX))
        );

        stats.put("newUsers", users.size());

        Map<String, Long> gradeDistribution = new LinkedHashMap<>();
        return stats;
    }

    public Map<String, Object> getAiUsageStats(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new LinkedHashMap<>();

        List<AiCallLog> logs = aiCallLogRepository.selectList(
                new LambdaQueryWrapper<AiCallLog>()
                        .ge(AiCallLog::getCreatedAt, startDate.atStartOfDay())
                        .le(AiCallLog::getCreatedAt, endDate.atTime(LocalTime.MAX))
        );

        stats.put("totalCalls", logs.size());
        stats.put("successCalls", logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.put("failedCalls", logs.stream().filter(l -> "failed".equals(l.getStatus())).count());

        Map<String, Long> providerStats = new LinkedHashMap<>();
        for (AiCallLog log : logs) {
            providerStats.merge(log.getProvider(), 1L, Long::sum);
        }
        stats.put("callsByProvider", providerStats);

        int totalTokens = logs.stream()
                .mapToInt(log -> log.getTotalTokens() != null ? log.getTotalTokens() : 0)
                .sum();
        stats.put("totalTokens", totalTokens);

        int avgLatency = logs.isEmpty() ? 0 :
                (int) logs.stream()
                        .filter(l -> l.getLatencyMs() != null)
                        .mapToInt(AiCallLog::getLatencyMs)
                        .average()
                        .orElse(0);
        stats.put("avgLatencyMs", avgLatency);

        return stats;
    }

    public Map<String, Object> getContentQualityStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long totalFeedbacks = userFeedbackRepository.selectCount(null);
        stats.put("totalFeedbacks", totalFeedbacks);

        long likeCount = userFeedbackRepository.selectCount(
                new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getType, "like")
        );
        long dislikeCount = userFeedbackRepository.selectCount(
                new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getType, "dislike")
        );

        stats.put("likes", likeCount);
        stats.put("dislikes", dislikeCount);
        stats.put("likeRate", (likeCount + dislikeCount) > 0 ?
                Math.round((double) likeCount / (likeCount + dislikeCount) * 10000) / 100.0 : 0);

        double avgRating = getAverageRating();
        stats.put("avgRating", avgRating);

        List<UserFeedback> recentReports = userFeedbackRepository.selectList(
                new LambdaQueryWrapper<UserFeedback>()
                        .eq(UserFeedback::getType, "report")
                        .orderByDesc(UserFeedback::getCreatedAt)
                        .last("LIMIT 10")
        );
        stats.put("recentReports", recentReports.size());

        return stats;
    }

    private double getAverageRating() {
        List<UserFeedback> feedbacksWithRating = userFeedbackRepository.selectList(
                new LambdaQueryWrapper<UserFeedback>()
                        .isNotNull(UserFeedback::getRating)
                        .gt(UserFeedback::getRating, 0)
        );
        if (feedbacksWithRating.isEmpty()) return 0.0;

        return feedbacksWithRating.stream()
                .mapToInt(UserFeedback::getRating)
                .average()
                .orElse(0.0);
    }

    public List<Map<String, Object>> getDailyActiveUsers(int days) {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date.toString());

            long activeUsers = notificationRepository.selectCount(
                    new LambdaQueryWrapper<Notification>()
                            .between(Notification::getCreatedAt,
                                    date.atStartOfDay(),
                                    date.plusDays(1).atStartOfDay())
            );

            dayData.put("activeUsers", activeUsers);
            result.add(dayData);
        }

        return result;
    }
}
