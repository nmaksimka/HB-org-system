package com.example.birthday.fundraiserservice.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateFundraiserRequest(
        @NotNull UUID beneficiaryId,
        @NotNull UUID giftId,
        @NotBlank @Size(max = 200) String title,
        @NotNull @DecimalMin("0.01") BigDecimal targetAmount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency) {}
