package com.example.birthday.giftservice.service;

import com.example.birthday.giftservice.dto.*;

import java.util.List;
import java.util.UUID;

public interface GiftService {
    GiftResponse create(UUID currentUserId, String correlationId, CreateGiftRequest request);
    List<GiftResponse> myWishlist(UUID currentUserId);
    List<GiftResponse> userWishlist(UUID currentUserId, UUID userId);
    GiftResponse update(UUID currentUserId, UUID giftId, String correlationId, UpdateGiftRequest request);
    void delete(UUID currentUserId, UUID giftId, String correlationId);
    GiftReservationResponse reserve(
            UUID currentUserId, UUID giftId, String correlationId, ReserveGiftRequest request);
}
