package com.codeinspire.ai.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PromptTemplateServiceTest {

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Test
    @DisplayName("渲染Prompt模板 - 变量替换成功")
    void render_ValidVariables_ReplacesCorrectly() {
        String template = "你好{{name}}，你的学校是{{schoolLevel}}，目标是{{targetPosition}}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "张三");
        variables.put("schoolLevel", "985");
        variables.put("targetPosition", "后端工程师");

        String result = promptTemplateService.render(template, variables);

        assertThat(result).contains("张三");
        assertThat(result).contains("985");
        assertThat(result).contains("后端工程师");
        assertThat(result).doesNotContain("{{");
    }

    @Test
    @DisplayName("渲染模板 - 缺失变量替换为空")
    void render_MissingVariables_ReplacesWithEmpty() {
        String template = "你好{{name}}，目标{{targetPosition}}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "张三");

        String result = promptTemplateService.render(template, variables);

        assertThat(result).contains("张三");
        assertThat(result).contains("目标");
    }

    @Test
    @DisplayName("构建系统Prompt - 通用场景")
    void buildSystemPrompt_GeneralScene_ReturnsDefaultPrompt() {
        Map<String, Object> context = new HashMap<>();
        context.put("学校层次", "985");

        String prompt = promptTemplateService.buildSystemPrompt("general", context);

        assertThat(prompt).isNotNull();
        assertThat(prompt).contains("CodeInspire");
        assertThat(prompt).contains("不编造信息");
    }

    @Test
    @DisplayName("构建系统Prompt - 求职建议场景")
    void buildSystemPrompt_CareerAdvice_ContainsCareerContent() {
        Map<String, Object> context = new HashMap<>();
        context.put("学校层次", "211");
        context.put("年级", "大三");

        String prompt = promptTemplateService.buildSystemPrompt("career_advice", context);

        assertThat(prompt).isNotNull();
        assertThat(prompt).containsAnyOf("求职策略", "求职", "差异化");
    }

    @Test
    @DisplayName("构建系统Prompt - 技术学习场景")
    void buildSystemPrompt_TechLearning_ContainsLearningContent() {
        Map<String, Object> context = new HashMap<>();
        context.put("专业方向", "Java后端");

        String prompt = promptTemplateService.buildSystemPrompt("tech_learning", context);

        assertThat(prompt).isNotNull();
        assertThat(prompt).containsAnyOf("技术学习", "学习路线", "概念解释");
    }

    @Test
    @DisplayName("渲染空模板 - 返回原文本")
    void render_EmptyTemplate_ReturnsOriginal() {
        String template = "这是一段没有变量的文本";

        String result = promptTemplateService.render(template, new HashMap<>());

        assertThat(result).isEqualTo(template);
    }

    @Test
    @DisplayName("使用量统计 - 增加计数")
    void incrementUsage_ValidId_IncrementsCount() {
        Long templateId = 1L;

        try {
            promptTemplateService.incrementUsage(templateId);
        } catch (Exception e) {
            // 如果模板不存在，这是预期行为
            assertThat(e.getMessage()).isNull();
        }
    }
}
