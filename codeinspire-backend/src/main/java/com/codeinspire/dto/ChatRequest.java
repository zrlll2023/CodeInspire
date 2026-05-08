package com.codeinspire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class ChatRequest {
    @NotBlank(message = "消息内容不能为空")
    private String message;
    private String sessionId;
    private String scene;
    private String provider;
    private Boolean stream;
}
