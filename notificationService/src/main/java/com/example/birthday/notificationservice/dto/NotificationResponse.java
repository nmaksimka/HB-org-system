package com.example.birthday.notificationservice.dto;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID userId,
        String type,
        String status,
        String title,
        String message,
        String relatedEntityType,
        UUID relatedEntityId,
        Instant sentAt,
        Instant createdAt
) {
}
