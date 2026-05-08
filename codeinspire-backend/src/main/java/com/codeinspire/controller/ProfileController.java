package com.codeinspire.controller;

import com.codeinspire.dto.ProfileInitRequest;
import com.codeinspire.entity.UserProfile;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.UserProfileService;
import com.codeinspire.vo.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ApiResponse<UserProfile> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfile profile = userProfileService.getProfileByUserId(userDetails.getId());
        return ApiResponse.success(profile);
    }

    @PostMapping("/init")
    public ApiResponse<UserProfile> initProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProfileInitRequest request) {
        UserProfile profile = userProfileService.initProfile(userDetails.getId(), request);
        return ApiResponse.success("画像初始化成功", profile);
    }

    @PutMapping
    public ApiResponse<UserProfile> updateProfile(
            @AuthenticationPrincipal CustomUserDetails UserDetails,
            @Valid @RequestBody ProfileInitRequest request) {
        UserProfile profile = userProfileService.updateProfile(UserDetails.getId(), request);
        return ApiResponse.success("画像更新成功", profile);
    }
}
