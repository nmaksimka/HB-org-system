package com.example.birthday.giftservice.controller;

import com.example.birthday.giftservice.dto.*;
import com.example.birthday.giftservice.service.GiftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/gifts")
public class GiftController {
    private final GiftService gifts;

    public GiftController(GiftService gifts) {
        this.gifts = gifts;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftResponse create(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @Valid @RequestBody CreateGiftRequest request) {
        return gifts.create(userId, correlationId, request);
    }

    @GetMapping("/me")
    public List<GiftResponse> mine(@RequestHeader("X-User-Id") UUID userId) {
        return gifts.myWishlist(userId);
    }

    @PutMapping("/{giftId}")
    public GiftResponse update(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID giftId,
            @Valid @RequestBody UpdateGiftRequest request) {
        return gifts.update(userId, giftId, correlationId, request);
    }

    @DeleteMapping("/{giftId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID giftId) {
        gifts.delete(userId, giftId, correlationId);
    }

    @PostMapping("/{giftId}/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftReservationResponse reserve(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID giftId,
            @Valid @RequestBody ReserveGiftRequest request) {
        return gifts.reserve(userId, giftId, correlationId, request);
    }
}
