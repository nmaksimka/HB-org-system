package com.example.birthday.giftservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record GiftResponse(
        UUID id,
        UUID userId,
        String title,
        String description,
        String link,
        String imageUrl,
        BigDecimal estimatedPrice,
        String currency,
        String priority,
        String visibility,
        String status,
        Instant createdAt
) {
}
