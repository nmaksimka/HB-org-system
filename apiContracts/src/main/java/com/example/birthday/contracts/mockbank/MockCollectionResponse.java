package com.example.birthday.contracts.mockbank;

import java.math.BigDecimal;
import java.util.UUID;

public record MockCollectionResponse(
        UUID id, UUID fundraiserId, UUID ownerId, BigDecimal targetAmount,
        String currency, String status, String paymentUrl) {}
