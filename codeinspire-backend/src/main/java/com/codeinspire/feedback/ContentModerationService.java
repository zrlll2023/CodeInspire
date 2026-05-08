package com.codeinspire.feedback;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentModerationService {

    @Data
    public static class ModerationResult {
        private boolean passed;
        private String category;
        private int riskLevel;
        private String reason;
        private List<String> detectedIssues;
        private Map<String, Object> metadata;
    }

    private static final List<String> SENSITIVE_POLITICAL = Arrays.asList(
            "推翻", "颠覆", "分裂", "恐怖主义", "极端主义"
    );

    private static final List<String> SENSITIVE_VIOLATION = Arrays.asList(
            "赌博", "色情", "毒品", "诈骗", "洗钱"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("1[3-9]\\d{9}");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("\\d{17}[\\dXx]");
    private Pattern PROMPT_INJECTION_PATTERN = Pattern.compile(
            "(?i)(忽略.*规则|你是.*现在|system.*prompt|显示.*指令)"
    );

    public ModerationResult moderateContent(String content) {
        if (content == null || content.isBlank()) {
            return createPassResult();
        }

        ModerationResult result = new ModerationResult();
        result.setDetectedIssues(new ArrayList<>());
        result.setMetadata(new HashMap<>());

        int maxRisk = 0;
        boolean blocked = false;

        if (checkPoliticalSensitive(content)) {
            result.getDetectedIssues().add("检测到政治敏感内容");
            result.setCategory("political");
            result.setRiskLevel(5);
            maxRisk = 5;
            blocked = true;
        }

        if (checkViolationContent(content)) {
            result.getDetectedIssues().add("检测到违规内容");
            if (result.getCategory() == null) result.setCategory("violation");
            result.setRiskLevel(Math.max(result.getRiskLevel(), 4));
            maxRisk = Math.max(maxRisk, 4);
            blocked = true;
        }

        if (checkPersonalInfo(content)) {
            result.getDetectedIssues().add("检测到个人隐私信息");
            result.getMetadata().put("hasPII", true);
            result.setRiskLevel(Math.max(result.getRiskLevel(), 2));
            maxRisk = Math.max(maxRisk, 2);
        }

        if (checkPromptInjection(content)) {
            result.getDetectedIssues().add("检测到可能的Prompt注入尝试");
            result.setCategory("security");
            result.setRiskLevel(Math.max(result.getRiskLevel(), 3));
            maxRisk = Math.max(maxRisk, 3);
            blocked = true;
        }

        if (!blocked) {
            result.setPassed(true);
            result.setReason("内容审核通过");
            result.setRiskLevel(0);
        } else {
            result.setPassed(false);
            result.setReason("内容未通过审核，包含: " + String.join(", ", result.getDetectedIssues()));
        }

        result.getMetadata().put("moderatedAt", new Date());
        return result;
    }

    private boolean checkPoliticalSensitive(String content) {
        for (String keyword : SENSITIVE_POLITICAL) {
            if (content.contains(keyword)) {
                log.warn("检测到政治敏感词: {}", keyword);
                return true;
            }
        }
        return false;
    }

    private boolean checkViolationContent(String content) {
        for (String keyword : SENSITIVE_VIOLATION) {
            if (content.contains(keyword)) {
                log.warn("检测到违规关键词: {}", keyword);
                return true;
            }
        }
        return false;
    }

    private boolean checkPersonalInfo(String content) {
        if (PHONE_PATTERN.matcher(content).find()) {
            log.info("检测到手机号");
            return true;
        }
        if (ID_CARD_PATTERN.matcher(content).find()) {
            log.info("检测到身份证号");
            return true;
        }
        return false;
    }

    private boolean checkPromptInjection(String content) {
        if (PROMPT_INJECTION_PATTERN.matcher(content).find()) {
            log.warn("检测到可能的Prompt注入");
            return true;
        }
        return false;
    }

    private ModerationResult createPassResult() {
        ModerationResult result = new ModerationResult();
        result.setPassed(true);
        result.setRiskLevel(0);
        result.setReason("空内容，自动通过");
        result.setDetectedIssues(new ArrayList<>());
        result.setMetadata(Map.of("moderatedAt", new Date()));
        return result;
    }

    public String sanitizeForDisplay(String content) {
        if (content == null) return null;

        String sanitized = content;
        sanitized = PHONE_PATTERN.matcher(sanitized).replaceAll("[手机号]");
        sanitized = ID_CARD_PATTERN.matcher(sanitized).replaceAll("[身份证号]");

        return sanitized;
    }
}
