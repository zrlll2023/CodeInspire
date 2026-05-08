package com.codeinspire.controller;

import com.codeinspire.dto.LoginRequest;
import com.codeinspire.dto.RegisterRequest;
import com.codeinspire.entity.User;
import com.codeinspire.service.AuthService;
import com.codeinspire.vo.ApiResponse;
import com.codeinspire.vo.JwtResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ApiResponse.success("注册成功", user);
    }

    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("登出成功", null);
    }
}
