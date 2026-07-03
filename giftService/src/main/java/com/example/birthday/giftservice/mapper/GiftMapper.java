package com.example.birthday.giftservice.mapper;

import com.example.birthday.giftservice.dto.*;
import com.example.birthday.giftservice.model.*;
import org.springframework.stereotype.Component;

@Component
public class GiftMapper {
    public GiftResponse toResponse(GiftWish gift) {
        return new GiftResponse(
                gift.getId(), gift.getUserId(), gift.getTitle(), gift.getDescription(),
                gift.getLink(), gift.getImageUrl(), gift.getEstimatedPrice(),
                gift.getCurrency(), gift.getPriority().name(), gift.getVisibility().name(),
                gift.getStatus().name(), gift.getCreatedAt());
    }

    public GiftReservationResponse toResponse(GiftReservation reservation) {
        return new GiftReservationResponse(
                reservation.getId(), reservation.getGift().getId(),
                reservation.getReservedByUserId(), reservation.getComment(),
                reservation.getCreatedAt());
    }
}
