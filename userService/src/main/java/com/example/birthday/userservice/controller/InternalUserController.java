package com.example.birthday.userservice.controller;

import com.example.birthday.contracts.user.UserShortResponse;
import com.example.birthday.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {
    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserShortResponse get(@PathVariable UUID userId) {
        return userService.getInternal(userId);
    }
}
