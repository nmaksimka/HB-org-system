package com.example.birthday.fundraiserservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ParticipantResponse(
        UUID id, UUID fundraiserId, UUID userId,
        BigDecimal contributedAmount, Instant joinedAt) {}
