package com.example.birthday.subscriptionservice.dto;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        UUID subscriberId,
        String type,
        UUID targetUserId,
        UUID targetGroupId,
        int daysBefore,
        boolean active,
        Instant createdAt
) {
}
