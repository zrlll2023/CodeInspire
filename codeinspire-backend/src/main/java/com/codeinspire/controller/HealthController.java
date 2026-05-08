package com.codeinspire.controller;

import com.codeinspire.monitoring.PerformanceMonitor;
import com.codeinspire.vo.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController implements HealthIndicator {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PerformanceMonitor performanceMonitor;

    @GetMapping
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", Instant.now().toString());
        health.put("service", "CodeInspire Backend");

        Map<String, Object> components = new HashMap<>();

        components.put("database", checkDatabase());
        if (redisTemplate != null) {
            components.put("redis", checkRedis());
        } else {
            components.put("redis", Map.of("status", "UNKNOWN", "message", "Redis未配置"));
        }

        health.put("components", components);
        return ApiResponse.success(health);
    }

    @GetMapping("/metrics")
    public ApiResponse<?> getMetrics() {
        return ApiResponse.success(performanceMonitor.getMetricsSummary());
    }

    private Map<String, Object> checkDatabase() {
        try {
            return Map.of("status", "UP", "type", "MySQL");
        } catch (Exception e) {
            return Map.of("status", "DOWN", "error", e.getMessage());
        }
    }

    private Map<String, Object> checkRedis() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            return Map.of("status", "UP");
        } catch (Exception e) {
            return Map.of("status", "DOWN", "error", e.getMessage());
        }
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "codeinspire")
                .withDetail("timestamp", System.currentTimeMillis())
                .build();
    }
}
