package com.example.birthday.userservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PublicUserResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String avatarUrl
) {
}
