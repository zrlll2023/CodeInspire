package com.codeinspire.abtest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbTestConfig {
    private String experimentId;
    private String name;
    private String description;
    private boolean enabled;
    private int trafficPercentage;
    private Map<String, Integer> variants;
    private Long startTime;
    private Long endTime;
    private String targetMetric;
}

@Data
public class AbTestResult {
    private String experimentId;
    private String assignedVariant;
    private boolean isInExperiment;
    private Long userId;
    private Map<String, Object> context;
}

@Service
public class AbTestService {

    private final Map<String, AbTestConfig> experiments = new ConcurrentHashMap<>();

    public AbTestService() {
        initDefaultExperiments();
    }

    private void initDefaultExperiments() {
        experiments.put("prompt_style_v2", AbTestConfig.builder()
                .experimentId("prompt_style_v2")
                .name("Prompt风格优化实验")
                .description("测试不同Prompt风格对用户满意度的影响")
                .enabled(true)
                .trafficPercentage(50)
                .variants(Map.of(
                        "control", 50,
                        "variant_a", 25,
                        "variant_b", 25
                ))
                .targetMetric("satisfaction_score")
                .build());

        experiments.put("ui_layout_test", AbTestConfig.builder()
                .experimentId("ui_layout_test")
                .name("UI布局测试")
                .description("测试不同的界面布局对用户留存的影响")
                .enabled(true)
                .trafficPercentage(30)
                .variants(Map.of(
                        "default", 70,
                        "compact", 30
                ))
                .targetMetric("session_duration")
                .build());

        experiments.put("onboarding_flow", AbTestConfig.builder()
                .experimentId("onboarding_flow")
                .name("引导流程优化")
                .description("测试不同的新用户引导流程")
                .enabled(false)
                .trafficPercentage(100)
                .variants(Map.of(
                        "current", 50,
                        "simplified", 50
                ))
                .targetMetric("profile_completion_rate")
                .build());
    }

    public AbTestResult assignVariant(Long userId, String experimentId) {
        AbTestResult result = new AbTestResult();
        result.setUserId(userId);
        result.setExperimentId(experimentId);

        AbTestConfig config = experiments.get(experimentId);
        if (config == null || !config.isEnabled()) {
            result.setAssignedVariant("control");
            result.setInExperiment(false);
            return result;
        }

        long hash = hashUserId(userId, experimentId);
        int hashPercent = (int) (hash % 100);

        if (hashPercent >= config.getTrafficPercentage()) {
            result.setAssignedVariant("control");
            result.setInExperiment(false);
            return result;
        }

        result.setInExperiment(true);
        String variant = selectVariant(hash, config.getVariants());
        result.setAssignedVariant(variant);

        Map<String, Object> context = new HashMap<>();
        context.put("experimentName", config.getName());
        context.put("assignedAt", System.currentTimeMillis());
        result.setContext(context);

        return result;
    }

    public List<AbTestConfig> getAllExperiments() {
        return new ArrayList<>(experiments.values());
    }

    public AbTestConfig getExperiment(String experimentId) {
        return experiments.get(experimentId);
    }

    public void updateExperiment(String experimentId, AbTestConfig config) {
        experiments.put(experimentId, config);
    }

    public void enableExperiment(String experimentId) {
        AbTestConfig config = experiments.get(experimentId);
        if (config != null) {
            config.setEnabled(true);
        }
    }

    public void disableExperiment(String experimentId) {
        AbTestConfig config = experiments.get(experimentId);
        if (config != null) {
            config.setEnabled(false);
        }
    }

    public Map<String, Object> getExperimentStats(String experimentId) {
        AbTestConfig config = experiments.get(experimentId);
        if (config == null) return Map.of("error", "实验不存在");

        return Map.of(
                "experimentId", experimentId,
                "name", config.getName(),
                "enabled", config.isEnabled(),
                "trafficPercentage", config.getTrafficPercentage(),
                "variants", config.getVariants(),
                "targetMetric", config.getTargetMetric(),
                "participantCount", 0,
                "conversions", Map.of()
        );
    }

    private long hashUserId(Long userId, String experimentId) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((userId + "_" + experimentId).getBytes());
            long hash = 0;
            for (int i = 0; i < Math.min(8, digest.length); i++) {
                hash |= ((long) (digest[i] & 0xff) << (i * 8));
            }
            return Math.abs(hash);
        } catch (Exception e) {
            return userId.hashCode();
        }
    }

    private String selectVariant(long hash, Map<String, Integer> variants) {
        int remaining = (int) (hash % 100);
        for (Map.Entry<String, Integer> entry : variants.entrySet()) {
            remaining -= entry.getValue();
            if (remaining <= 0) {
                return entry.getKey();
            }
        }
        return variants.keySet().iterator().next();
    }
}
