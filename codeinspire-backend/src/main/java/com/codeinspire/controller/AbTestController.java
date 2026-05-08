package com.codeinspire.controller;

import com.codeinspire.abtest.AbTestService;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/abtest")
@RequiredArgsConstructor
public class AbTestController {

    private final AbTestService abTestService;

    @GetMapping("/assign/{experimentId}")
    public ApiResponse<?> assignVariant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String experimentId) {
        var result = abTestService.assignVariant(userDetails.getId(), experimentId);
        return ApiResponse.success(result);
    }

    @GetMapping("/experiments")
    public ApiResponse<?> getAllExperiments() {
        return ApiResponse.success(abTestService.getAllExperiments());
    }

    @GetMapping("/experiments/{experimentId}")
    public ApiResponse<?> getExperiment(@PathVariable String experimentId) {
        return ApiResponse.success(abTestService.getExperiment(experimentId));
    }

    @GetMapping("/experiments/{experimentId}/stats")
    public ApiResponse<?> getExperimentStats(@PathVariable String experimentId) {
        return ApiResponse.success(abTestService.getExperimentStats(experimentId));
    }
}
