package com.codeinspire.privacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrivacyServiceTest {

    @Autowired
    private PrivacyService privacyService;

    @Test
    @DisplayName("检测手机号 - 成功")
    void desensitize_ContainsPhone_MasksPhone() {
        String text = "我的手机号是13812345678，请联系我";

        var result = privacyService.desensitize(text);

        assertThat(result.isHasSensitiveInfo()).isTrue();
        assertThat(result.getSanitizedText()).contains("[手机号]");
        assertThat(result.getSanitizedText()).doesNotContain("13812345678");
    }

    @Test
    @DisplayName("检测邮箱 - 成功")
    void desensitize_ContainsEmail_MasksEmail() {
        String text = "请发送到test@example.com";

        var result = privacyService.desensitize(text);

        assertThat(result.isHasSensitiveInfo()).isTrue();
        assertThat(result.getSanitizedText()).contains("[邮箱]");
        assertThat(result.getSanitizedText()).doesNotContain("test@example.com");
    }

    @Test
    @DisplayName("检测身份证号 - 成功")
    void desensitize_ContainsIdCard_MasksIdCard() {
        String text = "身份证号：110101199001011234";

        var result = privacyService.desensitize(text);

        assertThat(result.isHasSensitiveInfo()).isTrue();
        assertThat(result.getSanitizedText()).contains("[身份证号]");
    }

    @Test
    @DisplayName("无敏感信息 - 通过")
    void desensitize_NoSensitiveInfo_Passes() {
        String text = "这是一段普通的文本内容";

        var result = privacyService.desensitize(text);

        assertThat(result.isHasSensitiveInfo()).isFalse();
        assertThat(result.getSanitizedText()).isEqualTo(text);
    }

    @Test
    @DisplayName("空文本 - 返回空结果")
    void desensitize_EmptyText_ReturnsEmptyResult() {
        var result = privacyService.desensitize(null);

        assertThat(result.isHasSensitiveInfo()).isFalse();
        assertThat(result.getSanitizedText()).isNull();

        var emptyResult = privacyService.desensitize("");

        assertThat(emptyResult.isHasSensitiveInfo()).isFalse();
        assertThat(emptyResult.getSanitizedText()).isEqualTo("");
    }

    @Test
    @DisplayName("还原脱敏内容 - 成功")
    void restore_ValidMappings_RestoresOriginal() {
        String original = "联系我13812345678";
        var masked = privacyService.desensitize(original);
        var restored = privacyService.restore(masked.getSanitizedText(), masked.getMappings());

        assertThat(restored).contains("13812345678");
    }

    @Test
    @DisplayName("包含敏感信息检查 - 正确识别")
    void containsSensitiveInfo_HasPhone_ReturnsTrue() {
        assertThat(privacyService.containsSensitiveInfo("电话13812345678")).isTrue();
        assertThat(privacyService.containsSensitiveInfo("邮箱test@test.com")).isTrue();
        assertThat(privacyService.containsSensitiveInfo("正常文本")).isFalse();
        assertThat(privacyService.containsSensitiveInfo(null)).isFalse();
    }
}
