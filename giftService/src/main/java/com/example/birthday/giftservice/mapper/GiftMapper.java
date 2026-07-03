package com.example.birthday.giftservice.mapper;

import com.example.birthday.giftservice.dto.*;
import com.example.birthday.giftservice.model.*;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GiftMapper {
    GiftResponse toResponse(GiftWish gift);

    @Mapping(target = "giftId", source = "gift.id")
    GiftReservationResponse toResponse(GiftReservation reservation);
}
