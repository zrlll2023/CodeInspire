package com.codeinspire.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmergencyServiceTest {

    @Autowired
    private EmergencyService emergencyService;

    @Test
    @DisplayName("检测考研失败 - 识别为紧急场景")
    void detectAndHandle_ExamFailure_DetectsEmergency() {
        var response = emergencyService.detectAndHandle("我考研失败了，不知道该怎么办");

        assertThat(response.isEmergency()).isTrue();
        assertThat(response.getScenario().getType()).isEqualTo("exam_failure");
        assertThat(response.getResponseMessage()).contains("理解");
        assertThat(response.getActions()).isNotEmpty();
    }

    @Test
    @DisplayName("检测秋招失败 - 识别为紧急场景")
    void detectAndHandle_RecruitmentFailure_DetectsEmergency() {
        var response = emergencyService.detectAndHandle("秋招全部被拒了，找不到工作");

        assertThat(response.isEmergency()).isTrue();
        assertThat(response.getScenario().getType()).isEqualTo("recruitment_failure");
    }

    @Test
    @DisplayName("检测实习被裁 - 高严重度")
    void detectAndHandle_InternshipTerminated_HighSeverity() {
        var response = emergencyService.detectAndHandle("我的实习被突然裁员了");

        assertThat(response.isEmergency()).isTrue();
        assertThat(response.getScenario().getSeverityLevel()).isGreaterThanOrEqualTo(4);
    }

    @Test
    @DisplayName("检测情绪危机 - 最高严重度")
    void detectAndHandle_EmotionalCrisis_MaxSeverity() {
        var response = emergencyService.detectAndHandle("我不想活了，活着没意思");

        assertThat(response.isEmergency()).isTrue();
        assertThat(response.getScenario().getSeverityLevel()).isEqualTo(5);
        assertThat(response.getScenario().isRequiresHumanIntervention()).isTrue();
    }

    @Test
    @DisplayName("检测技术焦虑 - 中等严重度")
    void detectAndHandle_TechAnxiety_MediumSeverity() {
        var response = emergencyService.detectAndHandle("我觉得自己学的东西太少了，很焦虑");

        assertThat(response.isEmergency()).isTrue();
        assertThat(response.getScenario().getSeverityLevel()).isEqualTo(2);
    }

    @Test
    @DisplayName("普通消息 - 非紧急")
    void detectAndHandle_NormalMessage_NotEmergency() {
        var response = emergencyService.detectAndHandle("我想学习Spring Boot");

        assertThat(response.isEmergency()).isFalse();
    }

    @Test
    @DisplayName("空消息 - 非紧急")
    void detectAndHandle_EmptyMessage_NotEmergency() {
        var response = emergencyService.detectAndHandle("");

        assertThat(response.isEmergency()).isFalse();
    }

    @Test
    @DisplayName("获取所有应急场景 - 返回列表")
    void getAllScenarios_ReturnsAllScenarios() {
        var scenarios = emergencyService.getAllScenarios();

        assertThat(scenarios).isNotEmpty();
        assertThat(scenarios.size()).isGreaterThanOrEqualTo(6);
    }

    @Test
    @DisplayName("获取安慰消息 - 按用户类型返回不同内容")
    void getComfortingResponse_ByUserType_ReturnsDifferentContent() {
        String msg985 = emergencyService.getComfortingResponse("985");
        String msgNormal = emergencyService.getComfortingResponse("normal");

        assertThat(msg985).contains("985");
        assertThat(msgNormal).isNotNull();
    }
}
