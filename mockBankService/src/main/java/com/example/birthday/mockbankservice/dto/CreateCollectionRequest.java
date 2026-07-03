package com.example.birthday.mockbankservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateCollectionRequest(
        @NotNull UUID fundraiserId,
        @NotNull UUID ownerId,
        @NotNull @DecimalMin("0.01") BigDecimal targetAmount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency) {}
