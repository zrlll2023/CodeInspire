package com.codeinspire.service;

import com.codeinspire.entity.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InfoCollectionServiceTest {

    @Autowired
    private InfoCollectionService infoCollectionService;

    private Map<String, Object> completeProfile;
    private Map<String, Object> incompleteProfile;

    @BeforeEach
    void setUp() {
        completeProfile = new HashMap<>();
        completeProfile.put("schoolLevel", "985");
        completeProfile.put("grade", "大三");
        completeProfile.put("targetPosition", "后端开发");
        completeProfile.put("targetCityLevel", "一线城市");
        completeProfile.put("urgencyLevel", "紧急");
        completeProfile.put("weeklyAvailableHours", 20);
        completeProfile.put("skills", "[{\"name\":\"Java\",\"level\":\"advanced\"}]");

        incompleteProfile = new HashMap<>();
        incompleteProfile.put("schoolLevel", "985");
    }

    @Test
    @DisplayName("检查完整度 - 完整画像")
    void checkCompleteness_CompleteProfile_ReturnsComplete() {
        var result = infoCollectionService.checkCompleteness(completeProfile, "career_advice");

        assertThat(result.isComplete()).isTrue();
        assertThat(result.getCompletenessScore()).isEqualTo(1.0);
        assertThat(result.getMissingFields()).isEmpty();
    }

    @Test
    @DisplayName("检查完整度 - 不完整画像")
    void checkCompleteness_IncompleteProfile_ReturnsMissingFields() {
        var result = infoCollectionService.checkCompleteness(incompleteProfile, "career_advice");

        assertThat(result.isComplete()).isFalse();
        assertThat(result.getCompletenessScore()).isLessThan(1.0);
        assertThat(result.getMissingFields()).isNotEmpty();
        assertThat(result.getNextQuestion()).isNotNull();
    }

    @Test
    @DisplayName("获取优先问题 - 返回问题列表")
    void getPriorityQuestions_IncompleteProfile_ReturnsQuestions() {
        var questions = infoCollectionService.getPriorityQuestions(incompleteProfile, "career_advice", 3);

        assertThat(questions).isNotEmpty();
        assertThat(questions.size()).isLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("获取优先问题 - 完整画像返回空列表")
    void getPriorityQuestions_CompleteProfile_ReturnsEmptyList() {
        var questions = infoCollectionService.getPriorityQuestions(completeProfile, "career_advice", 5);

        assertThat(questions).isEmpty();
    }

    @Test
    @DisplayName("计算画像完整度 - 有数据")
    void calculateProfileCompleteness_WithData_ReturnsScore() {
        UserProfile profile = new UserProfile();
        profile.setSchoolLevel("985");
        profile.setGrade("大三");
        profile.setTargetPosition("后端开发");
        profile.setTargetCityLevel("一线城市");
        profile.setUrgencyLevel("紧急");
        profile.setWeeklyAvailableHours(20);
        profile.setMajorDirection("Java后端");
        profile.setCurrentStatus("在校");
        profile.setSkills("[{\"name\":\"Java\"}]");

        double score = infoCollectionService.calculateProfileCompleteness(profile);

        assertThat(score).isGreaterThan(0.5);
    }

    @Test
    @DisplayName("计算画像完整度 - 空数据")
    void calculateProfileCompleteness_NullProfile_ReturnsZero() {
        double score = infoCollectionService.calculateProfileCompleteness(null);

        assertThat(score).isEqualTo(0.0);
    }
}
