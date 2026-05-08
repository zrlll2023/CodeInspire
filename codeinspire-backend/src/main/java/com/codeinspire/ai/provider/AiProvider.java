package com.codeinspire.ai.provider;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface AiProvider {

    String getProviderName();

    String getModelName();

    boolean isAvailable();

    ChatResponse chat(ChatRequest request);

    Flux<String> chatStream(ChatRequest request);

    List<EmbeddingResponse> embed(List<String> texts);

    record ChatRequest(
            List<Message> messages,
            Double temperature,
            Integer maxTokens,
            Map<String, Object> extraParams
    ) {}

    record Message(
            String role,
            String content
    ) {
        public static Message system(String content) { return new Message("system", content); }
        public static Message user(String content) { return new Message("user", content); }
        public static Message assistant(String content) { return new Message("assistant", content); }
    }

    record ChatResponse(
            String content,
            String model,
            int inputTokens,
            int outputTokens,
            long latencyMs
    ) {}

    record EmbeddingResponse(
            int index,
            float[] vector
    ) {}
}
