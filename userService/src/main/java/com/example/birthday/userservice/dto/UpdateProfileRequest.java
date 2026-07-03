package com.example.birthday.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Past LocalDate birthDate,
        String avatarUrl,
        @Size(max = 2000) String bio,
        boolean birthDateVisible,
        boolean giftListVisible
) {
}
