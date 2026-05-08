package com.codeinspire.controller;

import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.TimeNodeReminderService;
import com.codeinspire.service.UserProfileService;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timenodes")
@RequiredArgsConstructor
public class TimeNodeController {

    private final TimeNodeReminderService timeNodeReminderService;
    private final UserProfileService userProfileService;

    @GetMapping("/reminders")
    public ApiResponse<?> getReminders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var profile = userProfileService.getProfileByUserId(userDetails.getId());
        var result = timeNodeReminderService.getRemindersForUser(profile);
        return ApiResponse.success(result);
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllTimeNodes() {
        return ApiResponse.success(timeNodeReminderService.getAllTimeNodes());
    }
}
