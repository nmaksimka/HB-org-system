package com.example.birthday.fundraiserservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FundraiserResponse(
        UUID id, UUID ownerId, UUID beneficiaryId, UUID giftId, String title,
        BigDecimal targetAmount, BigDecimal collectedAmount, String currency,
        String status, String paymentUrl, Instant createdAt) {}
