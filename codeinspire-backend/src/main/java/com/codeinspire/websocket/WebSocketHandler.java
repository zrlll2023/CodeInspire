package com.codeinspire.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = extractUserId(session);
        if (userId != null) {
            session.getAttributes().put("userId", userId);
            SESSIONS.put(userId + "_" + session.getId(), session);
            log.info("WebSocket连接建立: userId={}, sessionId={}", userId, session.getId());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        log.debug("收到WebSocket消息: {}", payload);

        try {
            Map<String, Object> msg = objectMapper.readValue(payload, Map.class);
            String type = (String) msg.get("type");

            if ("ping".equals(type)) {
                sendMessage(session, Map.of("type", "pong", "timestamp", System.currentTimeMillis()));
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误: sessionId={}", session.getId(), exception);
        removeSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket连接关闭: sessionId={}, status={}", session.getId(), status);
        removeSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendToUser(Long userId, Object message) {
        String prefix = userId + "_";
        SESSIONS.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .forEach(entry -> {
                    try {
                        if (entry.getValue().isOpen()) {
                            sendMessage(entry.getValue(), message);
                        } else {
                            SESSIONS.remove(entry.getKey());
                        }
                    } catch (Exception e) {
                        log.error("发送消息失败: {}", e.getMessage());
                    }
                });
    }

    public void broadcast(Object message) {
        SESSIONS.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    sendMessage(session, message);
                }
            } catch (Exception e) {
                log.error("广播消息失败: {}", e.getMessage());
            }
        });
    }

    public int getOnlineUserCount() {
        return (int) SESSIONS.keySet().stream()
                .map(key -> key.split("_")[0])
                .distinct()
                .count();
    }

    private void sendMessage(WebSocketSession session, Object message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
    }

    private void removeSession(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            SESSIONS.remove(userId + "_" + session.getId());
        }
    }

    private String extractUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            for (String param : query.split("&")) {
                if (param.startsWith("userId=")) {
                    return param.substring(7);
                }
            }
        }
        return null;
    }
}
