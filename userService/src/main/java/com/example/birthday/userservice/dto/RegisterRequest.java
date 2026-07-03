package com.example.birthday.userservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9_.-]{3,100}$") String username,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @NotNull @Past LocalDate birthDate
) {
}
