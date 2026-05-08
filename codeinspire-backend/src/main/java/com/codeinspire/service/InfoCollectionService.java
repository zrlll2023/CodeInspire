package com.codeinspire.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoCollectionService {

    private final ObjectMapper objectMapper;

    @Data
    public static class RequiredField {
        private String fieldKey;
        private String fieldName;
        private String category;
        private int priority;
        private String question;
        private List<String> options;
    }

    @Data
    public static class CollectionResult {
        private boolean complete;
        private List<RequiredField> missingFields;
        private String nextQuestion;
        private double completenessScore;
    }

    private static final Map<String, List<RequiredField>> SCENE_REQUIRED_FIELDS = Map.of(
            "career_advice", Arrays.asList(
                    new RequiredField("schoolLevel", "学校层次", "education", 1,
                            "你的学校是什么层次？", Arrays.asList("985/211", "一本", "二本", "民办本科", "专科")),
                    new RequiredField("grade", "年级", "time", 1,
                            "你现在是大几？", Arrays.asList("大一", "大二", "大三", "大四", "研究生")),
                    new RequiredField("targetPosition", "目标岗位", "career", 1,
                            "你想从事什么岗位？", Arrays.asList("后端开发", "前端开发", "全栈开发", "嵌入式", "运维", "测试开发")),
                    new RequiredField("targetCityLevel", "目标城市级别", "location", 2,
                            "你希望去哪个级别的城市发展？", Arrays.asList("一线城市", "新一线", "二三线")),
                    new RequiredField("urgencyLevel", "紧迫程度", "time", 2,
                            "你的求职紧迫程度如何？", Arrays.asList("紧急(6个月内)", "一般(1年内)", "充裕(2年+)")),
                    new RequiredField("skills", "技术技能", "tech", 3,
                            "你已经掌握了哪些技术？", null)
            ),
            "tech_learning", Arrays.asList(
                    new RequiredField("majorDirection", "专业方向", "career", 1,
                            "你想学习哪个方向？", Arrays.asList("Java后端", "Python", "Go", "前端", "AI/ML", "大数据")),
                    new RequiredField("schoolLevel", "学校层次", "education", 2,
                            "你的学校层次是？", Arrays.asList("985/211", "一本", "二本", "其他")),
                    new RequiredField("weeklyAvailableHours", "每周可用时间", "time", 2,
                            "每周能投入多少小时学习？", Arrays.asList("<10小时", "10-20小时", "20-30小时", ">30小时")),
                    new RequiredField("selfLearningAbility", "自学能力", "economic", 3,
                            "你觉得自己的自学能力如何？", Arrays.asList("强", "中", "弱"))
            ),
            "interview_prep", Arrays.asList(
                    new RequiredField("targetPosition", "目标岗位", "career", 1,
                            "你准备面试什么岗位？", Arrays.asList("Java后端", "Python后端", "前端", "算法工程师", "全栈")),
                    new RequiredField("schoolLevel", "学校层次", "education", 2,
                            "你的学校层次？", Arrays.asList("985/211", "一本", "二本", "其他")),
                    new RequiredField("projects", "项目经验", "tech", 2,
                            "你有相关项目经验吗？", null),
                    new RequiredField("urgencyLevel", "紧迫程度", "time", 3,
                            "面试时间有多紧急？", Arrays.asList("紧急(1个月内)", "一般(3个月内)", "充裕"))
            )
    );

    public CollectionResult checkCompleteness(Map<String, Object> userProfile, String scene) {
        List<RequiredField> requiredFields = SCENE_REQUIRED_FIELDS.getOrDefault(scene, SCENE_REQUIRED_FIELDS.get("general"));
        if (requiredFields == null) {
            requiredFields = Collections.emptyList();
        }

        List<RequiredField> missingFields = new ArrayList<>();
        for (RequiredField field : requiredFields) {
            Object value = userProfile.get(field.getFieldKey());
            if (value == null || (value instanceof String && ((String) value).isBlank())) {
                missingFields.add(field);
            }
        }

        double totalFields = requiredFields.size();
        double filledFields = totalFields - missingFields.size();
        double score = totalFields > 0 ? Math.round((filledFields / totalFields) * 100.0) / 100.0 : 0;

        CollectionResult result = new CollectionResult();
        result.setComplete(missingFields.isEmpty());
        result.setMissingFields(missingFields);
        result.setCompletenessScore(score);

        if (!missingFields.isEmpty()) {
            result.setNextQuestion(generateFollowUpQuestion(missingFields.get(0)));
        } else {
            result.setNextQuestion(null);
        }

        return result;
    }

    public String generateFollowUpQuestion(RequiredField field) {
        StringBuilder sb = new StringBuilder();
        sb.append("为了给你更精准的建议，我想了解一下：\n\n");
        sb.append("**").append(field.getFieldName()).append("**：").append(field.getQuestion()).append("\n");

        if (field.getOptions() != null && !field.getOptions().isEmpty()) {
            sb.append("\n选项：\n");
            for (int i = 0; i < field.getOptions().size(); i++) {
                sb.append(i + 1).append(". ").append(field.getOptions().get(i)).append("\n");
            }
        }

        return sb.toString();
    }

    public List<String> getPriorityQuestions(Map<String, Object> userProfile, String scene, int maxQuestions) {
        CollectionResult result = checkCompleteness(userProfile, scene);
        if (result.isComplete()) {
            return Collections.emptyList();
        }

        return result.getMissingFields().stream()
                .sorted(Comparator.comparingInt(RequiredField::getPriority))
                .limit(maxQuestions)
                .map(this::generateFollowUpQuestion)
                .collect(Collectors.toList());
    }

    public double calculateProfileCompleteness(com.codeinspire.entity.UserProfile profile) {
        if (profile == null) return 0.0;

        int totalFields = 15;
        int filledFields = 0;

        if (profile.getSchoolLevel() != null && !profile.getSchoolLevel().isBlank()) filledFields++;
        if (profile.getEducationLevel() != null && !profile.getEducationLevel().isBlank()) filledFields++;
        if (profile.getMajor() != null && !profile.getMajor().isBlank()) filledFields++;
        if (profile.getGrade() != null && !profile.getGrade().isBlank()) filledFields++;
        if (profile.getTargetPosition() != null && !profile.getTargetPosition().isBlank()) filledFields++;
        if (profile.getTargetCityLevel() != null && !profile.getTargetCityLevel().isBlank()) filledFields++;
        if (profile.getUrgencyLevel() != null && !profile.getUrgencyLevel().isBlank()) filledFields++;
        if (profile.getWeeklyAvailableHours() != null) filledFields++;
        if (profile.getMajorDirection() != null && !profile.getMajorDirection().isBlank()) filledFields++;
        if (profile.getCurrentStatus() != null && !profile.getCurrentStatus().isBlank()) filledFields++;
        if (profile.getSkills() != null && !profile.getSkills().equals("[]")) filledFields++;
        if (profile.getProjects() != null && !profile.getProjects().equals("[]")) filledFields++;

        return Math.round((double) filledFields / totalFields * 10000) / 100.0;
    }
}
