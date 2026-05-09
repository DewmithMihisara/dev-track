package com.dev.dev_track.service;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.request.LoginRequest;
import com.dev.dev_track.dto.request.RegisterRequest;
import com.dev.dev_track.dto.response.LoginResponse;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<LoginResponse> register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ApiResponse.fail("Email is already in use");
        }

        UserEntity user = UserEntity.builder()
                .userName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();

        userRepo.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ApiResponse.ok("User registered successfully", LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getDisplayName())
                .build());
    }

    public ApiResponse<LoginResponse> login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserEntity user = userRepo.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ApiResponse.ok("Login successful", LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getDisplayName())
                .build());
    }
}
