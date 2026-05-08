package com.codeinspire.privacy;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PrivacyService {

    private final Map<String, String> sensitivePatterns = new ConcurrentHashMap<>();

    public PrivacyService() {
        sensitivePatterns.put("phone", Pattern.compile("1[3-9]\\d{9}"));
        sensitivePatterns.put("email", Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"));
        sensitivePatterns.put("id_card", Pattern.compile("\\d{17}[\\dXx]"));
    }

    @Data
    public static class DesensitizationResult {
        private String sanitizedText;
        private List<PrivacyMapping> mappings;
        private boolean hasSensitiveInfo;
    }

    @Data
    public static class PrivacyMapping {
        private String type;
        private String originalValue;
        private String placeholder;
        private int position;
    }

    public DesensitizationResult desensitize(String text) {
        if (text == null || text.isEmpty()) {
            DesensitizationResult result = new DesensitizationResult();
            result.setSanitizedText(text);
            result.setMappings(new ArrayList<>());
            result.setHasSensitiveInfo(false);
            return result;
        }

        String processedText = text;
        List<PrivacyMapping> mappings = new ArrayList<>();
        int offset = 0;

        for (Map.Entry<String, Pattern> entry : sensitivePatterns.entrySet()) {
            String type = entry.getKey();
            Pattern pattern = entry.getValue();
            Matcher matcher = pattern.matcher(processedText);

            while (matcher.find()) {
                String originalValue = matcher.group();
                String placeholder = "[" + getTypeLabel(type) + "]";
                int position = matcher.start() + offset;

                PrivacyMapping mapping = new PrivacyMapping();
                mapping.setType(type);
                mapping.setOriginalValue(originalValue);
                mapping.setPlaceholder(placeholder);
                mapping.setPosition(position);
                mappings.add(mapping);

                processedText = new StringBuilder(processedText)
                        .replace(matcher.start(), matcher.end(), placeholder)
                        .toString();

                offset += placeholder.length() - originalValue.length();
            }
        }

        DesensitizationResult result = new DesensitizationResult();
        result.setSanitizedText(processedText);
        result.setMappings(mappings);
        result.setHasSensitiveInfo(!mappings.isEmpty());
        return result;
    }

    public String restore(String sanitizedText, List<PrivacyMapping> mappings) {
        if (mappings == null || mappings.isEmpty() || sanitizedText == null) {
            return sanitizedText;
        }

        String result = sanitizedText;
        for (int i = mappings.size() - 1; i >= 0; i--) {
            PrivacyMapping mapping = mappings.get(i);
            result = result.replace(mapping.getPlaceholder(), mapping.getOriginalValue());
        }
        return result;
    }

    public boolean containsSensitiveInfo(String text) {
        if (text == null) return false;
        for (Pattern pattern : sensitivePatterns.values()) {
            if (pattern.matcher(text).find()) {
                return true;
            }
        }
        return false;
    }

    private String getTypeLabel(String type) {
        return switch (type) {
            case "phone" -> "手机号";
            case "email" -> "邮箱";
            case "id_card" -> "身份证号";
            default -> "敏感信息";
        };
    }
}
