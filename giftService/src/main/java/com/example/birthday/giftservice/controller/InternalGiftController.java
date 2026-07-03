package com.example.birthday.giftservice.controller;

import com.example.birthday.contracts.gift.GiftShortResponse;
import com.example.birthday.giftservice.exception.BusinessException;
import com.example.birthday.giftservice.repository.GiftWishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/internal/gifts")
@RequiredArgsConstructor
public class InternalGiftController {
    private final GiftWishRepository gifts;

    @GetMapping("/{giftId}")
    GiftShortResponse get(@PathVariable UUID giftId) {
        var gift = gifts.findByIdAndDeletedAtIsNull(giftId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Gift not found"));
        return new GiftShortResponse(
                gift.getId(), gift.getUserId(), gift.getTitle(), gift.getEstimatedPrice(),
                gift.getCurrency(), gift.getStatus().name());
    }
}
