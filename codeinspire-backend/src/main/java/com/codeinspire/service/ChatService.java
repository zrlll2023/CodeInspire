package com.codeinspire.service;

import com.codeinspire.ai.provider.AiProvider;
import com.codeinspire.ai.provider.AiProvider.ChatRequest;
import com.codeinspire.ai.provider.AiProvider.Message;
import com.codeinspire.ai.service.AiCallLogService;
import com.codeinspire.ai.service.AiRouterService;
import com.codeinspire.ai.template.PromptTemplateService;
import com.codeinspire.dto.ChatRequest;
import com.codeinspire.entity.Conversation;
import com.codeinspire.entity.Message;
import com.codeinspire.entity.UserProfile;
import com.codeinspire.privacy.PrivacyService;
import com.codeinspire.repository.ConversationRepository;
import com.codeinspire.repository.MessageRepository;
import com.codeinspire.vo.ChatResponse as VoChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final AiRouterService aiRouterService;
    private final AiCallLogService aiCallLogService;
    private final PromptTemplateService promptTemplateService;
    private final PrivacyService privacyService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserProfileService userProfileService;

    public VoChatResponse chat(Long userId, ChatRequest request) {
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        Conversation conversation = getOrCreateConversation(userId, sessionId, request.getScene());

        saveUserMessage(conversation.getId(), request.getMessage());

        PrivacyService.DesensitizationResult desensitizationResult = privacyService.desensitize(request.getMessage());
        String sanitizedMessage = desensitizationResult.getSanitizedText();

        UserProfile profile = userProfileService.getProfileByUserId(userId);
        Map<String, Object> context = buildContext(profile);

        String scene = request.getScene() != null ? request.getScene() : "general";
        String systemPrompt = promptTemplateService.buildSystemPrompt(scene, context);

        List<Message> historyMessages = getRecentMessages(conversation.getId(), 10);
        List<AiProvider.Message> messages = new ArrayList<>();
        messages.add(Message.system(systemPrompt));
        for (Message msg : historyMessages) {
            messages.add("user".equals(msg.getRole()) ? Message.user(msg.getContent()) : Message.assistant(msg.getContent()));
        }
        messages.add(Message.user(sanitizedMessage));

        AiProvider provider = aiRouterService.getProviderByScenario(scene);
        if (request.getProvider() != null) {
            provider = aiRouterService.getProvider(request.getProvider());
        }

        long startTime = System.currentTimeMillis();
        try {
            AiProvider.ChatResponse response = provider.chat(
                    new ChatRequest(messages, 0.7, null, null)
            );

            String restoredResponse = privacyService.restore(response.content(), desensitizationResult.getMappings());
            saveAssistantMessage(conversation.getId(), restoredResponse);

            int latencyMs = (int) (System.currentTimeMillis() - startTime);
            aiCallLogService.logCall(aiCallLogService.createSuccessLog(
                    userId, provider.getProviderName(), provider.getModelName(),
                    null, response.inputTokens(), response.outputTokens(), latencyMs
            ));

            return new VoChatResponse(
                    restoredResponse,
                    sessionId,
                    provider.getProviderName(),
                    provider.getModelName(),
                    response.inputTokens(),
                    response.outputTokens(),
                    (long) latencyMs
            );
        } catch (Exception e) {
            log.error("AI 调用失败: {}", e.getMessage());
            aiCallLogService.logCall(aiCallLogService.createFailureLog(
                    userId, provider.getProviderName(), provider.getModelName(), e.getMessage()
            ));
            throw new RuntimeException("AI 服务暂时不可用，请稍后重试");
        }
    }

    private Conversation getOrCreateConversation(Long userId, String sessionId, String type) {
        Conversation existing = conversationRepository.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Conversation>()
                        .eq(Conversation::getUserId, userId)
                        .eq(Conversation::getSessionId, sessionId)
        );

        if (existing != null) {
            return existing;
        }

        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setSessionId(sessionId);
        conversation.setType(type != null ? type : "general");
        conversationRepository.insert(conversation);
        return conversation;
    }

    private void saveUserMessage(Long conversationId, String content) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setRole("user");
        message.setContent(content);
        messageRepository.insert(message);
    }

    private void saveAssistantMessage(Long conversationId, String content) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setRole("assistant");
        message.setContent(content);
        messageRepository.insert(message);
    }

    private List<Message> getRecentMessages(Long conversationId, int limit) {
        return messageRepository.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Message>()
                        .eq(Message::getConversationId, conversationId)
                        .orderByAsc(Message::getCreatedAt)
                        .last("LIMIT " + limit)
        );
    }

    private Map<String, Object> buildContext(UserProfile profile) {
        Map<String, Object> context = new HashMap<>();
        if (profile != null) {
            context.put("学校层次", profile.getSchoolLevel());
            context.put("年级", profile.getGrade());
            context.put("专业方向", profile.getMajorDirection());
            context.put("目标岗位", profile.getTargetPosition());
            context.put("目标城市", profile.getTargetCityLevel());
            context.put("紧迫程度", profile.getUrgencyLevel());
            context.put("每周可用时间", profile.getWeeklyAvailableHours() + "小时");
        }
        return context;
    }
}
