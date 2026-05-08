package com.codeinspire.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String content;
    private String sessionId;
    private String provider;
    private String model;
    private Integer inputTokens;
    private Integer outputTokens;
    private Long latencyMs;
}
