package com.example.birthday.giftservice.dto;

import com.example.birthday.giftservice.model.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateGiftRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 3000) String description,
        String link,
        String imageUrl,
        @DecimalMin("0.00") BigDecimal estimatedPrice,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currency,
        @NotNull GiftPriority priority,
        @NotNull GiftVisibility visibility
) {
}
