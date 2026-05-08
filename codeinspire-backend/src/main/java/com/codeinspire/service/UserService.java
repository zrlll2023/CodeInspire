package com.codeinspire.service;

import com.codeinspire.dto.RegisterRequest;
import com.codeinspire.entity.User;
import com.codeinspire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.insert(user);
        return user;
    }

    public User findByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User findById(Long id) {
        return userRepository.selectById(id);
    }
}
