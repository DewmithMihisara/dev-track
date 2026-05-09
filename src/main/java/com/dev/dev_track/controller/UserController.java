package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ApiResponse;
import com.dev.dev_track.dto.UserDto;
import com.dev.dev_track.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User profile operations")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(201).body(userService.saveUser(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        return ResponseEntity.ok(userService.updateUser(userDto));
    }
}
