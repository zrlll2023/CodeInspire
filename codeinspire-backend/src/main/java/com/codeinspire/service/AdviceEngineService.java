package com.codeinspire.service;

import com.codeinspire.ai.provider.AiProvider;
import com.codeinspire.ai.provider.AiProvider.ChatRequest;
import com.codeinspire.ai.provider.AiProvider.Message;
import com.codeinspire.ai.service.AiRouterService;
import com.codeinspire.ai.template.PromptTemplateService;
import com.codeinspire.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdviceEngineService {

    private final AiRouterService aiRouterService;
    private final PromptTemplateService promptTemplateService;

    @Data
    public static class AdviceRequest {
        private String scene;
        private String userQuestion;
        private UserProfile userProfile;
        private Map<String, Object> additionalContext;
    }

    @Data
    public static class AdviceResponse {
        private String advice;
        private String confidenceLevel;
        private List<String> keyPoints;
        private List<String> riskWarnings;
        private List<String> actionItems;
        private Map<String, Object> metadata;
    }

    public AdviceResponse generateAdvice(AdviceRequest request) {
        String scene = request.getScene() != null ? request.getScene() : "general";
        Map<String, Object> context = buildContextMap(request.getUserProfile());
        if (request.getAdditionalContext() != null) {
            context.putAll(request.getAdditionalContext());
        }

        String systemPrompt = promptTemplateService.buildSystemPrompt(scene, context);

        String userPrompt;
        if (request.getUserQuestion() != null && !request.getUserQuestion().isBlank()) {
            userPrompt = request.getUserQuestion();
        } else {
            userPrompt = generateDefaultQuestion(scene, context);
        }

        List<AiProvider.Message> messages = new ArrayList<>();
        messages.add(Message.system(systemPrompt));
        messages.add(Message.user(userPrompt));

        try {
            AiProvider provider = aiRouterService.getProviderByScenario(scene);
            AiProvider.ChatResponse response = provider.chat(
                    new ChatRequest(messages, 0.8, null, null)
            );

            AdviceResponse adviceResponse = parseAdviceResponse(response.content(), scene);
            return adviceResponse;
        } catch (Exception e) {
            log.error("建议生成失败: {}", e.getMessage());
            return createFallbackAdvice(request);
        }
    }

    private Map<String, Object> buildContextMap(UserProfile profile) {
        Map<String, Object> context = new HashMap<>();
        if (profile == null) return context;

        context.put("学校层次", profile.getSchoolLevel());
        context.put("学校类型", profile.getSchoolType());
        context.put("学历", profile.getEducationLevel());
        context.put("专业", profile.getMajor());
        context.put("年级", profile.getGrade());
        context.put("紧迫程度", profile.getUrgencyLevel());
        context.put("每周可用时间", profile.getWeeklyAvailableHours() + "小时");
        context.put("课业压力", profile.getCourseworkPressure());
        context.put("目标城市级别", profile.getTargetCityLevel());
        context.put("产业偏好", profile.getIndustryPreference());
        context.put("付费意愿", profile.getPaymentWillingness());
        context.put("自学能力", profile.getSelfLearningAbility());
        context.put("经济压力", profile.getEconomicPressure());
        context.put("当前位置", profile.getCurrentStatus());
        context.put("专业方向", profile.getMajorDirection());
        context.put("目标岗位", profile.getTargetPosition());
        context.put("目标企业", profile.getTargetCompany());
        context.put("期望薪资", profile.getExpectedSalary());

        return context;
    }

    private String generateDefaultQuestion(String scene, Map<String, Object> context) {
        return switch (scene) {
            case "career_advice" -> "请根据我的背景，为我制定一份详细的求职策略和准备计划";
            case "tech_learning" -> "请根据我的背景和目标方向，为我规划一条学习路线";
            case "interview_prep" -> "请帮我分析面试准备的重点和可能的面试题";
            default -> "请根据我的情况给出专业的建议";
        };
    }

    private AdviceResponse parseAdviceResponse(String content, String scene) {
        AdviceResponse response = new AdviceResponse();
        response.setAdvice(content);
        response.setConfidenceLevel(determineConfidenceLevel(content));
        response.setKeyPoints(extractKeyPoints(content));
        response.setRiskWarnings(extractRiskWarnings(content));
        response.setActionItems(extractActionItems(content));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("scene", scene);
        metadata.put("generatedAt", new Date());
        response.setMetadata(metadata);

        return response;
    }

    private String determineConfidenceLevel(String content) {
        if (content.contains("可能") || content.contains("大概") || content.contains("推测")) {
            return "medium";
        } else if (content.contains("不确定") || content.contains("需要更多信息")) {
            return "low";
        }
        return "high";
    }

    private List<String> extractKeyPoints(String content) {
        List<String> points = new ArrayList<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.matches(".*[一二三四五六七八九十]\\..*|.*[1-9]\\.\\s.*|.*[-*]\\s.*")) {
                points.add(line.trim());
                if (points.size() >= 5) break;
            }
        }
        return points;
    }

    private List<String> extractRiskWarnings(String content) {
        List<String> warnings = new ArrayList<>();
        if (content.contains("注意") || content.contains("风险") || content.contains("避免")) {
            warnings.add("请仔细阅读建议中的注意事项部分");
        }
        return warnings;
    }

    private List<String> extractActionItems(String content) {
        List<String> actions = new ArrayList<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().contains("建议") || line.toLowerCase().contains("可以") || line.toLowerCase().contains("应该")) {
                actions.add(line.trim());
                if (actions.size() >= 3) break;
            }
        }
        return actions;
    }

    private AdviceResponse createFallbackAdvice(AdviceRequest request) {
        AdviceResponse fallback = new AdviceResponse();
        fallback.setAdvice("抱歉，AI服务暂时不可用。基于您的画像信息，我建议您先完善个人资料，然后再次尝试获取个性化建议。");
        fallback.setConfidenceLevel("low");
        fallback.setKeyPoints(List.of("请稍后重试"));
        fallback.setRiskWarnings(List.of("服务暂时不可用"));
        fallback.setActionItems(List.of("检查网络连接", "联系技术支持"));
        return fallback;
    }
}
