package com.example.birthday.contracts.user;

import java.time.LocalDate;
import java.util.UUID;

public record UserShortResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String status
) {
}
