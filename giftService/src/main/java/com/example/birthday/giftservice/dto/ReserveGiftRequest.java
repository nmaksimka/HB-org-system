package com.example.birthday.giftservice.dto;

import jakarta.validation.constraints.Size;

public record ReserveGiftRequest(@Size(max = 1000) String comment) {
}
