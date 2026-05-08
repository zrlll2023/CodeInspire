package com.codeinspire.feedback;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.entity.UserFeedback;
import com.codeinspire.repository.UserFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UserFeedbackRepository userFeedbackRepository;

    public UserFeedback createFeedback(Long userId, Long messageId, String type,
                                         Integer rating, String comment, Map<String, Object> details) {
        UserFeedback feedback = new UserFeedback();
        feedback.setUserId(userId);
        feedback.setMessageId(messageId);
        feedback.setType(type);
        feedback.setRating(rating);
        feedback.setComment(comment);

        if (details != null && !details.isEmpty()) {
            try {
                feedback.setFeedbackDetails(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(details));
            } catch (Exception e) {
                feedback.setFeedbackDetails(null);
            }
        }

        feedback.setStatus("pending");
        userFeedbackRepository.insert(feedback);

        return feedback;
    }

    public List<UserFeedback> getUserFeedbacks(Long userId) {
        return userFeedbackRepository.selectList(
                new LambdaQueryWrapper<UserFeedback>()
                        .eq(UserFeedback::getUserId, userId)
                        .orderByDesc(UserFeedback::getCreatedAt)
        );
    }

    public UserFeedback getFeedbackById(Long feedbackId) {
        return userFeedbackRepository.selectById(feedbackId);
    }

    public void addAdminReply(Long feedbackId, String reply) {
        UserFeedback feedback = userFeedbackRepository.selectById(feedbackId);
        if (feedback != null) {
            feedback.setAdminReply(reply);
            feedback.setStatus("resolved");
            feedback.setUpdatedAt(LocalDateTime.now());
            userFeedbackRepository.updateById(feedback);
        }
    }

    public void updateStatus(Long feedbackId, String status) {
        UserFeedback feedback = userFeedbackRepository.selectById(feedbackId);
        if (feedback != null) {
            feedback.setStatus(status);
            feedback.setUpdatedAt(LocalDateTime.now());
            userFeedbackRepository.updateById(feedback);
        }
    }

    public List<UserFeedback> getAllFeedbacks(String status, int page, int size) {
        LambdaQueryWrapper<UserFeedback> query = new LambdaQueryWrapper<UserFeedback>()
                .orderByDesc(UserFeedback::getCreatedAt);

        if (status != null && !status.isEmpty()) {
            query.eq(UserFeedback::getStatus, status);
        }

        int offset = (page - 1) * size;
        query.last("LIMIT " + size + " OFFSET " + offset);

        return userFeedbackRepository.selectList(query);
    }

    public long countByStatus(String status) {
        if (status != null && !status.isEmpty()) {
            return userFeedbackRepository.selectCount(
                    new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getStatus, status)
            );
        }
        return userFeedbackRepository.selectCount(null);
    }

    public double getAverageRating() {
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

    public Map<String, Long> getFeedbackStats() {
        long total = userFeedbackRepository.selectCount(null);
        long pending = userFeedbackRepository.selectCount(
                new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getStatus, "pending")
        );
        long resolved = userFeedbackRepository.selectCount(
                new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getStatus, "resolved")
        );
        long reviewed = userFeedbackRepository.selectCount(
                new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getStatus, "reviewed")
        );

        return Map.of(
                "total", total,
                "pending", pending,
                "resolved", resolved,
                "reviewed", reviewed
        );
    }
}
