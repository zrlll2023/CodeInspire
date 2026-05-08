package com.codeinspire.controller;

import com.codeinspire.dto.ChatRequest;
import com.codeinspire.security.CustomUserDetails;
import com.codeinspire.service.ChatService;
import com.codeinspire.vo.ApiResponse;
import com.codeinspire.vo.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ApiResponse<ChatResponse> send(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.chat(userDetails.getId(), request);
        return ApiResponse.success(response);
    }

    @GetMapping("/history")
    public ApiResponse<?> getHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestParam String sessionId) {
        return ApiResponse.success(null);
    }
}
