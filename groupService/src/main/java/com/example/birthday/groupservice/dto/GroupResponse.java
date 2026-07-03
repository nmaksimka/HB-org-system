package com.example.birthday.groupservice.dto;

import java.time.Instant;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String name,
        String description,
        UUID ownerId,
        String avatarUrl,
        boolean publicGroup,
        Instant createdAt
) {
}
