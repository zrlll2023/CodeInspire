package com.codeinspire.config;

import com.codeinspire.ai.provider.AiProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    private String defaultProvider = "deepseek";
    private int timeoutSeconds = 60;
    private int maxRetries = 3;
    private Map<String, ProviderConfig> providers;

    @Data
    public static class ProviderConfig {
        private boolean enabled = true;
        private String apiKey;
        private String baseUrl;
        private String model;
        private int maxTokens = 4096;
    }
}
