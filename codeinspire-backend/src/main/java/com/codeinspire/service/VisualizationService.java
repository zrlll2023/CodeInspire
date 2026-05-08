package com.codeinspire.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeinspire.entity.SkillAssessment;
import com.codeinspire.entity.UserGrowthSnapshot;
import com.codeinspire.repository.SkillAssessmentRepository;
import com.codeinspire.repository.UserGrowthSnapshotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisualizationService {

    private final SkillAssessmentRepository skillAssessmentRepository;
    private final UserGrowthSnapshotRepository userGrowthSnapshotRepository;
    private final ObjectMapper objectMapper;

    @lombok.Data
    public static class RadarChartData {
        private List<String> labels;
        private List<Integer> values;
        private String title;
        private LocalDate date;
    }

    @lombok.Data
    public static class ProgressData {
        private List<LocalDate> dates;
        private List<Double> overallScores;
        private Map<String, List<Integer>> skillProgress;
        private int completedTasks;
        private int totalTasks;
    }

    @lombok.Data
    public static class SkillRadarPoint {
        private String category;
        private String name;
        private int level;
        private int maxLevel = 100;
    }

    public RadarChartData getSkillRadarChart(Long userId) {
        List<SkillAssessment> assessments = skillAssessmentRepository.selectList(
                new LambdaQueryWrapper<SkillAssessment>()
                        .eq(SkillAssessment::getUserId, userId)
                        .orderByDesc(SkillAssessment::getAssessedAt)
        );

        Map<String, Integer> latestLevels = new LinkedHashMap<>();
        for (SkillAssessment assessment : assessments) {
            if (!latestLevels.containsKey(assessment.getSkillCategory())) {
                latestLevels.put(assessment.getSkillCategory(), assessment.getLevel());
            }
        }

        List<String> defaultCategories = Arrays.asList("编程语言", "数据库", "框架", "系统设计", "算法", "项目经验");
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (String category : defaultCategories) {
            labels.add(category);
            values.add(latestLevels.getOrDefault(category, 0));
        }

        RadarChartData chartData = new RadarChartData();
        chartData.setLabels(labels);
        chartData.setValues(values);
        chartData.setTitle("能力雷达图");
        chartData.setDate(LocalDate.now());

        return chartData;
    }

    public ProgressData getLearningProgress(Long userId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);

        List<UserGrowthSnapshot> snapshots = userGrowthSnapshotRepository.selectList(
                new LambdaQueryWrapper<UserGrowthSnapshot>()
                        .eq(UserGrowthSnapshot::getUserId, userId)
                        .ge(UserGrowthSnapshot::getSnapshotDate, startDate)
                        .orderByAsc(UserGrowthSnapshot::getSnapshotDate)
        );

        ProgressData progressData = new ProgressData();
        progressData.setDates(snapshots.stream()
                .map(UserGrowthSnapshot::getSnapshotDate)
                .collect(Collectors.toList()));

        progressData.setOverallScores(new ArrayList<>());
        progressData.setSkillProgress(new HashMap<>());

        for (UserGrowthSnapshot snapshot : snapshots) {
            try {
                Map<String, Object> skillSummary = objectMapper.readValue(
                        snapshot.getSkillSummary(),
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
                );

                double overallScore = 0;
                if (skillSummary.get("overall") instanceof Number) {
                    overallScore = ((Number) skillSummary.get("overall")).doubleValue();
                }
                progressData.getOverallScores().add(overallScore);

                if (skillSummary.get("skills") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> skills = (Map<String, Object>) skillSummary.get("skills");
                    skills.forEach((key, value) -> {
                        progressData.getSkillProgress()
                                .computeIfAbsent(key, k -> new ArrayList<>())
                                .add(value instanceof Number ? ((Number) value).intValue() : 0);
                    });
                }
            } catch (JsonProcessingException e) {
                log.error("解析技能快照失败: {}", e.getMessage());
            }
        }

        return progressData;
    }

    public void recordSkillAssessment(Long userId, String category, String name, int level, String method) {
        SkillAssessment assessment = new SkillAssessment();
        assessment.setUserId(userId);
        assessment.setSkillCategory(category);
        assessment.setSkillName(name);
        assessment.setLevel(level);
        assessment.setAssessmentMethod(method != null ? method : "self_report");

        skillAssessmentRepository.insert(assessment);
    }

    public void createGrowthSnapshot(Long userId) {
        List<SkillAssessment> assessments = skillAssessmentRepository.selectList(
                new LambdaQueryWrapper<SkillAssessment>()
                        .eq(SkillAssessment::getUserId, userId)
                        .orderByDesc(SkillAssessment::getAssessedAt)
        );

        Map<String, Object> summary = new HashMap<>();
        Map<String, Integer> latestSkills = new LinkedHashMap<>();
        int totalLevel = 0;
        int count = 0;

        for (SkillAssessment assessment : assessments) {
            String key = assessment.getSkillCategory() + ":" + assessment.getSkillName();
            if (!latestSkills.containsKey(key)) {
                latestSkills.put(key, assessment.getLevel());
                totalLevel += assessment.getLevel();
                count++;
            }
        }

        summary.put("overall", count > 0 ? totalLevel / count : 0);
        summary.put("skills", latestSkills);
        summary.put("assessmentCount", count);
        summary.put("timestamp", LocalDateTime.now().toString());

        try {
            UserGrowthSnapshot snapshot = new UserGrowthSnapshot();
            snapshot.setUserId(userId);
            snapshot.setSnapshotDate(LocalDate.now());
            snapshot.setSkillSummary(objectMapper.writeValueAsString(summary));

            userGrowthSnapshotRepository.insert(snapshot);
        } catch (JsonProcessingException e) {
            log.error("创建成长快照失败: {}", e.getMessage());
        }
    }

    public List<SkillRadarPoint> getDetailedSkillLevels(Long userId) {
        List<SkillAssessment> assessments = skillAssessmentRepository.selectList(
                new LambdaQueryWrapper<SkillAssessment>()
                        .eq(SkillAssessment::getUserId, userId)
                        .orderByDesc(SkillAssessment::getAssessedAt)
        );

        Map<String, SkillRadarPoint> latestMap = new LinkedHashMap<>();
        for (SkillAssessment assessment : assessments) {
            String key = assessment.getSkillName();
            if (!latestMap.containsKey(key)) {
                SkillRadarPoint point = new SkillRadarPoint();
                point.setCategory(assessment.getSkillCategory());
                point.setName(assessment.getSkillName());
                point.setLevel(assessment.getLevel());
                latestMap.put(key, point);
            }
        }

        return new ArrayList<>(latestMap.values());
    }

    public Map<String, Object> getProfileAnalysis(Long userId, com.codeinspire.entity.UserProfile profile) {
        Map<String, Object> analysis = new HashMap<>();

        analysis.put("profileCompleteness", calculateCompleteness(profile));
        analysis.put("strengths", analyzeStrengths(profile));
        analysis.put("weaknesses", analyzeWeaknesses(profile));
        analysis.put("recommendations", generateRecommendations(profile));
        analysis.put("riskFactors", identifyRiskFactors(profile));

        return analysis;
    }

    private double calculateCompleteness(com.codeinspire.entity.UserProfile profile) {
        if (profile == null) return 0.0;

        int totalFields = 12;
        int filled = 0;

        if (isFilled(profile.getSchoolLevel())) filled++;
        if (isFilled(profile.getEducationLevel())) filled++;
        if (isFilled(profile.getMajor())) filled++;
        if (isFilled(profile.getGrade())) filled++;
        if (isFilled(profile.getTargetPosition())) filled++;
        if (isFilled(profile.getTargetCityLevel())) filled++;
        if (isFilled(profile.getUrgencyLevel())) filled++;
        if (profile.getWeeklyAvailableHours() != null) filled++;
        if (isFilled(profile.getMajorDirection())) filled++;
        if (isFilled(profile.getCurrentStatus())) filled++;

        return Math.round((double) filled / totalFields * 10000) / 100.0;
    }

    private boolean isFilled(String value) {
        return value != null && !value.isBlank();
    }

    private List<String> analyzeStrengths(com.codeinspire.entity.UserProfile profile) {
        List<String> strengths = new ArrayList<>();
        if (profile != null) {
            if ("985/211".equals(profile.getSchoolLevel())) {
                strengths.add("学校背景优势明显，大厂认可度高");
            }
            if (profile.getWeeklyAvailableHours() != null && profile.getWeeklyAvailableHours() >= 20) {
                strengths.add("学习时间充足，可进行深度学习");
            }
            if ("充裕".equals(profile.getUrgencyLevel()) || "一般".equals(profile.getUrgencyLevel())) {
                strengths.add("时间相对充裕，可以系统性提升");
            }
            if (isFilled(profile.getProjects()) && !profile.getProjects().equals("[]")) {
                strengths.add("有项目经验积累");
            }
        }
        return strengths.isEmpty() ? List.of("请完善个人信息以便分析") : strengths;
    }

    private List<String> analyzeWeaknesses(com.codeinspire.entity.UserProfile profile) {
        List<String> weaknesses = new ArrayList<>();
        if (profile != null) {
            if ("紧急".equals(profile.getUrgencyLevel())) {
                weaknesses.add("时间紧迫，需要高效规划");
            }
            if (profile.getWeeklyAvailableHours() != null && profile.getWeeklyAvailableHours() < 10) {
                weaknesses.add("学习时间较少，需聚焦核心技能");
            }
            if ("民办本科".equals(profile.getSchoolLevel()) || "专科".equals(profile.getSchoolLevel())) {
                weaknesses.add("学历背景可能存在劣势，建议通过项目和竞赛弥补");
            }
        }
        return weaknesses.isEmpty() ? List.of("暂无明显短板") : weaknesses;
    }

    private List<String> generateRecommendations(com.codeinspire.entity.UserProfile profile) {
        List<String> recommendations = new ArrayList<>();
        if (profile != null) {
            if ("大三".equals(profile.getGrade()) || "大四".equals(profile.getGrade())) {
                recommendations.add("当前是关键时期，建议优先准备实习和秋招");
            }
            if ("后端开发".equals(profile.getTargetPosition()) || "全栈".equals(profile.getTargetPosition())) {
                recommendations.add("重点掌握Spring Boot、MySQL、Redis等核心技术栈");
            }
            if (profile.getWeeklyAvailableHours() != null && profile.getWeeklyAvailableHours() < 15) {
                recommendations.add("利用碎片化时间，推荐使用LeetCode每日刷题");
            }
        }
        return recommendations.isEmpty() ? List.of("完善画像后将获得个性化建议") : recommendations;
    }

    private List<String> identifyRiskFactors(com.codeinspire.entity.UserProfile profile) {
        List<String> risks = new ArrayList<>();
        if (profile != null) {
            if ("紧急".equals(profile.getUrgencyLevel()) && !isFilled(profile.getSkills())) {
                risks.add("时间紧迫但技术储备不足，风险较高");
            }
            if ("大四".equals(profile.getGrade()) && "待业".equals(profile.getCurrentStatus())) {
                risks.add("毕业季临近但未确定去向");
            }
        }
        return risks.isEmpty() ? List.of("暂无显著风险因素") : risks;
    }
}
