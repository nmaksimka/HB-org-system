package com.example.birthday.mockbankservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id, UUID collectionId, UUID payerId, BigDecimal amount,
        String status, Instant createdAt) {}
