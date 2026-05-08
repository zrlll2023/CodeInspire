package com.codeinspire.ai.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class QwenProvider implements AiProvider {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final int maxTokens;

    public QwenProvider(
            @Value("${ai.providers.qwen.api-key}") String apiKey,
            @Value("${ai.providers.qwen.base-url}") String baseUrl,
            @Value("${ai.providers.qwen.model}") String model,
            @Value("${ai.providers.qwen.max-tokens}") int maxTokens) {
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String getProviderName() { return "qwen"; }

    @Override
    public String getModelName() { return model; }

    @Override
    public boolean isAvailable() { return !apiKey.equals("your-qwen-api-key"); }

    @Override
    public ChatResponse chat(ChatRequest request) {
        long startTime = System.currentTimeMillis();

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", request.messages().stream()
                .map(m -> Map.of("role", m.role(), "content", m.content()))
                .toList());
        body.put("temperature", request.temperature() != null ? request.temperature() : 0.7);
        body.put("max_tokens", request.maxTokens() != null ? request.maxTokens() : maxTokens);

        try {
            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(Duration.ofSeconds(60));

            if (response != null && response.has("choices")) {
                String content = response.get("choices").get(0).get("message").get("content").asText();
                int inputTokens = response.has("usage") ? response.get("usage").get("prompt_tokens").asInt() : 0;
                int outputTokens = response.has("usage") ? response.get("usage").get("completion_tokens").asInt() : 0;
                return new ChatResponse(content, model, inputTokens, outputTokens, System.currentTimeMillis() - startTime);
            }
        } catch (Exception e) {
            log.error("通义千问 API 调用失败: {}", e.getMessage());
        }
        throw new RuntimeException("通义千问 API 调用失败");
    }

    @Override
    public Flux<String> chatStream(ChatRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", request.messages().stream()
                .map(m -> Map.of("role", m.role(), "content", m.content()))
                .toList());
        body.put("temperature", request.temperature() != null ? request.temperature() : 0.7);
        body.put("max_tokens", request.maxTokens() != null ? request.maxTokens() : maxTokens);
        body.put("stream", true);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(chunk -> chunk.contains("\"content\""))
                .map(this::extractContent);
    }

    private String extractContent(String chunk) {
        try {
            JsonNode node = objectMapper.readTree(chunk);
            if (node.has("choices") && node.get("choices").size() > 0 &&
                    node.get("choices").get(0).has("delta")) {
                JsonNode delta = node.get("choices").get(0).get("delta");
                if (delta.has("content")) {
                    return delta.get("content").asText();
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    @Override
    public List<EmbeddingResponse> embed(List<String> texts) {
        throw new UnsupportedOperationException("通义千问 暂不支持 Embedding API");
    }
}
