package com.codeinspire.service;

import com.codeinspire.dto.LoginRequest;
import com.codeinspire.dto.RegisterRequest;
import com.codeinspire.entity.User;
import com.codeinspire.security.JwtTokenProvider;
import com.codeinspire.vo.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public User register(RegisterRequest request) {
        return userService.register(request);
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsernameOrEmail(request.getUsername());
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        return new JwtResponse(token, "Bearer", user.getId(), user.getUsername(), user.getEmail());
    }
}
