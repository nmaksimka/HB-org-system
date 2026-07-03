package com.example.birthday.userservice.controller;

import com.example.birthday.userservice.dto.UpdateProfileRequest;
import com.example.birthday.userservice.dto.UserResponse;
import com.example.birthday.userservice.dto.PublicUserResponse;
import com.example.birthday.userservice.security.AuthenticatedUser;
import com.example.birthday.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<PublicUserResponse> list(
            @RequestParam(defaultValue = "") String search, Pageable pageable) {
        return userService.list(search, pageable);
    }

    @GetMapping("/{userId}")
    public PublicUserResponse get(@PathVariable UUID userId) {
        return userService.getPublicProfile(userId);
    }

    @PutMapping("/me/profile")
    public UserResponse updateProfile(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(currentUser.id(), request);
    }
}
