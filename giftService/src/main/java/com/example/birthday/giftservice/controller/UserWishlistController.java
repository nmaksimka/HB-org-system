package com.example.birthday.giftservice.controller;

import com.example.birthday.giftservice.dto.GiftResponse;
import com.example.birthday.giftservice.service.GiftService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/gifts")
public class UserWishlistController {
    private final GiftService gifts;

    public UserWishlistController(GiftService gifts) {
        this.gifts = gifts;
    }

    @GetMapping
    public List<GiftResponse> wishlist(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @PathVariable UUID userId) {
        return gifts.userWishlist(currentUserId, userId);
    }
}
