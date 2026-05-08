package com.codeinspire.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Aspect
@Component
public class PerformanceMonitor {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, MetricData> metrics = new ConcurrentHashMap<>();

    @Around("@annotation(org.springframework.web.bind.annotation.*Mapping) || execution(* com.codeinspire.controller..*(..))")
    public Object monitorApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long startTime = System.nanoTime();

        try {
            Object result = joinPoint.proceed();
            long duration = System.nanoTime() - startTime;

            recordMetric(methodName, duration, "success", null);
            log.debug("API调用: {} 耗时: {}ms", methodName, Duration.ofNanos(duration).toMillis());

            return result;
        } catch (Throwable e) {
            long duration = System.nanoTime() - startTime;
            recordMetric(methodName, duration, "error", e.getClass().getSimpleName());
            throw e;
        }
    }

    private void recordMetric(String method, long durationNs, String status, String errorType) {
        metrics.computeIfAbsent(method, k -> new MetricData())
                .record(Duration.ofNanos(durationNs).toMillis(), status);

        if (Duration.ofNanos(durationNs).toMillis() > 3000) {
            log.warn("慢接口告警: {} 耗时: {}ms 状态: {}", method,
                    Duration.ofNanos(durationNs).toMillis(), status);
        }
    }

    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("timestamp", Instant.now().toString());

        Map<String, Object> apiMetrics = new HashMap<>();
        for (Map.Entry<String, MetricData> entry : metrics.entrySet()) {
            apiMetrics.put(entry.getKey(), entry.getValue().getSummary());
        }
        summary.put("apis", apiMetrics);

        return summary;
    }

    public void resetMetrics() {
        metrics.clear();
    }

    static class MetricData {
        private final AtomicLong totalCalls = new AtomicLong(0);
        private final AtomicLong totalDurationMs = new AtomicLong(0);
        private final AtomicLong successCalls = new AtomicLong(0);
        private final AtomicLong errorCalls = new AtomicLong(0);
        private volatile long maxDurationMs = 0;
        private volatile long minDurationMs = Long.MAX_VALUE;

        void record(long durationMs, String status) {
            totalCalls.incrementAndGet();
            totalDurationMs.addAndGet(durationMs);

            if ("success".equals(status)) {
                successCalls.incrementAndGet();
            } else {
                errorCalls.incrementAndGet();
            }

            updateMaxMin(durationMs);
        }

        synchronized void updateMaxMin(long durationMs) {
            if (durationMs > maxDurationMs) maxDurationMs = durationMs;
            if (durationMs < minDurationMs) minDurationMs = durationMs;
        }

        Map<String, Object> getSummary() {
            long total = totalCalls.get();
            long avg = total > 0 ? totalDurationMs.get() / total : 0;

            return Map.of(
                    "totalCalls", total,
                    "avgDurationMs", avg,
                    "maxDurationMs", maxDurationMs == Long.MAX_VALUE ? 0 : maxDurationMs,
                    "minDurationMs", minDurationMs == Long.MAX_VALUE ? 0 : minDurationMs,
                    "successRate", total > 0 ? Math.round((double) successCalls.get() / total * 10000) / 100.0 : 0,
                    "errorCount", errorCalls.get()
            );
        }
    }
}
