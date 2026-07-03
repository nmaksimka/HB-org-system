package com.example.birthday.userservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String username,
        String role,
        String status,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String avatarUrl,
        String bio,
        boolean birthDateVisible,
        boolean giftListVisible
) {
}
