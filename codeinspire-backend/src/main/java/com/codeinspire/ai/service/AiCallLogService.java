package com.codeinspire.ai.service;

import com.codeinspire.entity.AiCallLog;
import com.codeinspire.repository.AiCallLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiCallLogService {

    private final AiCallLogRepository aiCallLogRepository;

    public void logCall(AiCallLog log) {
        try {
            aiCallLogRepository.insert(log);
        } catch (Exception e) {
            // 日志记录失败不影响主流程
        }
    }

    public AiCallLog createSuccessLog(Long userId, String provider, String model,
                                       Long promptTemplateId, Integer inputTokens,
                                       Integer outputTokens, int latencyMs) {
        AiCallLog log = new AiCallLog();
        log.setUserId(userId);
        log.setProvider(provider);
        log.setModel(model);
        log.setPromptTemplateId(promptTemplateId);
        log.setInputTokens(inputTokens);
        log.setOutputTokens(outputTokens);
        log.setTotalTokens(inputTokens + outputTokens);
        log.setLatencyMs(latencyMs);
        log.setStatus("success");
        return log;
    }

    public AiCallLog createFailureLog(Long userId, String provider, String model,
                                        String errorMessage) {
        AiCallLog log = new AiCallLog();
        log.setUserId(userId);
        log.setProvider(provider);
        log.setModel(model);
        log.setStatus("failed");
        log.setErrorMessage(errorMessage);
        return log;
    }
}
