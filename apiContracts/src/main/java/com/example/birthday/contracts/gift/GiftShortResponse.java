package com.example.birthday.contracts.gift;

import java.math.BigDecimal;
import java.util.UUID;

public record GiftShortResponse(
        UUID id, UUID userId, String title, BigDecimal estimatedPrice,
        String currency, String status) {}
