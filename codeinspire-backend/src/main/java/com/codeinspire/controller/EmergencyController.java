package com.codeinspire.controller;

import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.EmergencyService;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;

    @PostMapping("/detect")
    public ApiResponse<?> detectEmergency(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        var response = emergencyService.detectAndHandle(message);
        return ApiResponse.success(response);
    }

    @GetMapping("/scenarios")
    public ApiResponse<?> getScenarios() {
        return ApiResponse.success(emergencyService.getAllScenarios());
    }

    @GetMapping("/comfort")
    public ApiResponse<?> getComfortMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String userType) {
        String message = emergencyService.getComfortingResponse(userType);
        return ApiResponse.success(Map.of("message", message));
    }
}
