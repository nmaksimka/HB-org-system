package com.example.birthday.contracts.mockbank;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateMockCollectionRequest(
        UUID fundraiserId, UUID ownerId, BigDecimal targetAmount, String currency) {}
