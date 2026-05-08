package com.codeinspire.controller;

import com.codeinspire.feedback.FeedbackService;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ApiResponse<?> createFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> request) {
        Long messageId = request.get("messageId") != null ?
                Long.valueOf(request.get("messageId").toString()) : null;
        String type = (String) request.getOrDefault("type", "like");
        Integer rating = request.get("rating") != null ?
                Integer.valueOf(request.get("rating").toString()) : null;
        String comment = (String) request.get("comment");

        Map<String, Object> details = new java.util.HashMap<>();
        if (request.containsKey("details")) {
            details = (Map<String, Object>) request.get("details");
        }

        var feedback = feedbackService.createFeedback(
                userDetails.getId(), messageId, type, rating, comment, details);
        return ApiResponse.success("反馈提交成功", feedback);
    }

    @GetMapping("/my")
    public ApiResponse<List<?>> getMyFeedbacks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var feedbacks = feedbackService.getUserFeedbacks(userDetails.getId());
        return ApiResponse.success(feedbacks);
    }
}
