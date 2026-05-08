package com.codeinspire.ai.service;

import com.codeinspire.ai.provider.AiProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiRouterService {

    @Value("${ai.default-provider:deepseek}")
    private String defaultProvider;

    private final List<AiProvider> providers;

    public AiRouterService(List<AiProvider> providers) {
        this.providers = providers;
    }

    public AiProvider getProvider(String providerName) {
        if (providerName != null && !providerName.isEmpty()) {
            return providers.stream()
                    .filter(p -> p.getProviderName().equals(providerName) && p.isAvailable())
                    .findFirst()
                    .orElse(getDefaultProvider());
        }
        return getDefaultProvider();
    }

    public AiProvider getDefaultProvider() {
        return providers.stream()
                .filter(p -> p.getProviderName().equals(defaultProvider) && p.isAvailable())
                .findFirst()
                .orElseGet(() -> providers.stream()
                        .filter(AiProvider::isAvailable)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("没有可用的AI模型")));
    }

    public AiProvider getProviderByScenario(String scenario) {
        return switch (scenario) {
            case "simple_qa" -> getProvider("mimo");
            case "tech_explanation" -> getProvider("zhipu");
            case "code_generation" -> getProvider("qwen");
            case "deep_planning", "career_advice" -> getProvider("deepseek");
            default -> getDefaultProvider();
        };
    }
}
