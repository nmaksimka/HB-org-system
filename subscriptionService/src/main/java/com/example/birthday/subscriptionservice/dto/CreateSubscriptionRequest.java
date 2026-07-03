package com.example.birthday.subscriptionservice.dto;

import jakarta.validation.constraints.*;

public record CreateSubscriptionRequest(
        @Min(0) @Max(365) int daysBefore
) {
}
