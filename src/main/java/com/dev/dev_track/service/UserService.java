package com.dev.dev_track.service;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.UserDto;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.exception.DuplicateResourceException;
import com.dev.dev_track.exception.ResourceNotFoundException;
import com.dev.dev_track.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<Void> saveUser(UserDto userDto) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }
        userRepo.save(UserEntity.builder()
                .userName(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .isActive(true)
                .build());
        return ApiResponse.ok("User saved successfully");
    }

    public ApiResponse<Void> updateUser(UserDto userDto) {
        UserEntity user = userRepo.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(userDto.getEmail())
                && userRepo.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        user.setUserName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        userRepo.save(user);
        return ApiResponse.ok("User updated successfully");
    }
}
