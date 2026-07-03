package com.example.birthday.giftservice.dto;

import java.time.Instant;
import java.util.UUID;

public record GiftReservationResponse(
        UUID id,
        UUID giftId,
        UUID reservedByUserId,
        String comment,
        Instant createdAt
) {
}
