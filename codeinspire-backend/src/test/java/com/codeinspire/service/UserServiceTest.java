package com.codeinspire.service;

import com.codeinspire.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RegisterRequest();
        validRequest.setUsername("testuser");
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");
    }

    @Test
    @DisplayName("注册新用户 - 成功")
    void register_NewUser_Success() {
        var user = userService.register(validRequest);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPasswordHash()).isNotNull().isNotEqualTo("password123");
    }

    @Test
    @DisplayName("注册重复用户名 - 失败")
    void register_DuplicateUsername_Fails() {
        userService.register(validRequest);

        RegisterRequest duplicateRequest = new RegisterRequest();
        duplicateRequest.setUsername("testuser");
        duplicateRequest.setEmail("different@example.com");
        duplicateRequest.setPassword("password456");

        assertThatThrownBy(() -> userService.register(duplicateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("用户名已存在");
    }

    @Test
    @DisplayName("注册重复邮箱 - 失败")
    void register_DuplicateEmail_Fails() {
        userService.register(validRequest);

        RegisterRequest duplicateRequest = new RegisterRequest();
        duplicateRequest.setUsername("differentuser");
        duplicateRequest.setEmail("test@example.com");
        duplicateRequest.setPassword("password456");

        assertThatThrownBy(() -> userService.register(duplicateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("邮箱已被注册");
    }

    @Test
    @DisplayName("查找用户 - 成功")
    void findByUsernameOrEmail_ExistingUser_ReturnsUser() {
        userService.register(validRequest);

        var found = userService.findByUsernameOrEmail("testuser");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("查找不存在的用户 - 失败")
    void findByUsernameOrEmail_NonExistent_ThrowsException() {
        assertThatThrownBy(() -> userService.findByUsernameOrEmail("nonexistent"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("用户不存在");
    }
}
