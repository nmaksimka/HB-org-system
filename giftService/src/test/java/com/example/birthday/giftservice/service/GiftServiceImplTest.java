package com.example.birthday.giftservice.service;

import com.example.birthday.giftservice.client.UserClient;
import com.example.birthday.giftservice.dto.ReserveGiftRequest;
import com.example.birthday.giftservice.exception.BusinessException;
import com.example.birthday.giftservice.mapper.GiftMapper;
import com.example.birthday.giftservice.model.GiftWish;
import com.example.birthday.giftservice.repository.*;
import com.example.birthday.giftservice.service.impl.GiftServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftServiceImplTest {
    @Mock GiftWishRepository gifts;
    @Mock GiftReservationRepository reservations;
    @Mock UserClient users;
    @Mock OutboxService outbox;
    private GiftServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GiftServiceImpl(
                gifts, reservations, users, Mappers.getMapper(GiftMapper.class), outbox);
    }

    @Test
    void ownerCannotReserveOwnGift() {
        UUID ownerId = UUID.randomUUID();
        UUID giftId = UUID.randomUUID();
        var gift = new GiftWish();
        gift.setUserId(ownerId);
        when(gifts.findForUpdate(giftId)).thenReturn(Optional.of(gift));

        assertThatThrownBy(() -> service.reserve(
                ownerId, giftId, "correlation", new ReserveGiftRequest(null)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User cannot reserve own gift");
    }

    @Test
    void userCannotEditForeignGift() {
        UUID ownerId = UUID.randomUUID();
        UUID attackerId = UUID.randomUUID();
        UUID giftId = UUID.randomUUID();
        var gift = new GiftWish();
        gift.setUserId(ownerId);
        when(gifts.findByIdAndDeletedAtIsNull(giftId)).thenReturn(Optional.of(gift));

        assertThatThrownBy(() -> service.delete(attackerId, giftId, "correlation"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Gift belongs to another user");
    }
}
