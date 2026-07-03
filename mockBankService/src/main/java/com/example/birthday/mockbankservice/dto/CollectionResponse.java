package com.example.birthday.mockbankservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CollectionResponse(
        UUID id, UUID fundraiserId, UUID ownerId, BigDecimal targetAmount,
        String currency, String status, String paymentUrl) {}
