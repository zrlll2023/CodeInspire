package com.codeinspire.ai.template;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptTemplateService {

    private final PromptTemplateRepository promptTemplateRepository;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    public PromptTemplate getPromptByScene(String scene) {
        return promptTemplateRepository.selectOne(
                new LambdaQueryWrapper<PromptTemplate>()
                        .eq(PromptTemplate::getScene, scene)
                        .eq(PromptTemplate::getStatus, "active")
                        .orderByDesc(PromptTemplate::getVersion)
                        .last("LIMIT 1")
        );
    }

    public String render(String templateContent, Map<String, Object> variables) {
        String result = templateContent;
        Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);

        while (matcher.find()) {
            String varName = matcher.group(1);
            Object value = variables.get(varName);
            if (value != null) {
                result = result.replace("{{" + varName + "}}", value.toString());
            } else {
                result = result.replace("{{" + varName + "}}", "");
            }
        }

        return result;
    }

    public String buildSystemPrompt(String scene, Map<String, Object> context) {
        PromptTemplate template = getPromptByScene(scene);
        if (template == null) {
            log.warn("未找到场景 {} 的Prompt模板，使用默认模板", scene);
            return buildDefaultSystemPrompt(scene, context);
        }
        return render(template.getContent(), context);
    }

    private String buildDefaultSystemPrompt(String scene, Map<String, Object> context) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是CodeInspire，一个专业的计算机专业学生AI个性化顾问。\n\n");
        sb.append("## 核心原则\n");
        sb.append("1. 不编造信息，基于事实回答\n");
        sb.append("2. 信息不足时主动追问\n");
        sb.append("3. 低置信度时明确提示用户\n");
        sb.append("4. 提供可执行的具体建议\n\n");

        switch (scene) {
            case "career_advice":
                sb.append("## 求职建议场景\n");
                sb.append("- 根据用户背景提供差异化求职策略\n");
                sb.append("- 分析目标岗位的技能要求\n");
                sb.append("- 提供具体的学习路线和准备计划\n");
                break;
            case "tech_learning":
                sb.append("## 技术学习场景\n");
                sb.append("- 解释技术概念时使用类比\n");
                sb.append("- 提供代码示例和实践建议\n");
                sb.append("- 推荐适合的学习资源\n");
                break;
            case "interview_prep":
                sb.append("## 面试准备场景\n");
                sb.append("- 提供常见面试题和解题思路\n");
                sb.append("- 模拟面试场景进行练习\n");
                sb.append("- 针对性提升薄弱环节\n");
                break;
            default:
                sb.append("## 通用场景\n");
                sb.append("- 热情、专业、耐心地回答问题\n");
                break;
        }

        if (context != null && !context.isEmpty()) {
            sb.append("\n## 用户背景信息\n");
            context.forEach((key, value) -> sb.append("- ").append(key).append(": ").append(value).append("\n"));
        }

        sb.append("\n请用中文回答，保持简洁实用。");
        return sb.toString();
    }

    public void incrementUsage(Long promptId) {
        PromptTemplate template = promptTemplateRepository.selectById(promptId);
        if (template != null) {
            template.setUsageCount(template.getUsageCount() + 1);
            promptTemplateRepository.updateById(template);
        }
    }
}
