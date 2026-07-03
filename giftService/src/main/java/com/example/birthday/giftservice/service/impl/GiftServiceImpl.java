package com.example.birthday.giftservice.service.impl;

import com.example.birthday.giftservice.client.UserClient;
import com.example.birthday.giftservice.dto.*;
import com.example.birthday.giftservice.exception.BusinessException;
import com.example.birthday.giftservice.mapper.GiftMapper;
import com.example.birthday.giftservice.model.*;
import com.example.birthday.giftservice.repository.*;
import com.example.birthday.giftservice.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class GiftServiceImpl implements GiftService {
    private final GiftWishRepository gifts;
    private final GiftReservationRepository reservations;
    private final UserClient users;
    private final GiftMapper mapper;
    private final OutboxService outbox;

    public GiftServiceImpl(
            GiftWishRepository gifts,
            GiftReservationRepository reservations,
            UserClient users,
            GiftMapper mapper,
            OutboxService outbox) {
        this.gifts = gifts;
        this.reservations = reservations;
        this.users = users;
        this.mapper = mapper;
        this.outbox = outbox;
    }

    @Override
    @Transactional
    public GiftResponse create(
            UUID userId, String correlationId, CreateGiftRequest request) {
        users.getUser(userId);
        var gift = new GiftWish();
        gift.setUserId(userId);
        apply(gift, request.title(), request.description(), request.link(),
                request.imageUrl(), request.estimatedPrice(), request.currency(),
                request.priority(), request.visibility());
        gift = gifts.saveAndFlush(gift);
        append("gift.created", "GiftCreatedEvent", gift, correlationId);
        return mapper.toResponse(gift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftResponse> myWishlist(UUID userId) {
        return gifts.findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftResponse> userWishlist(UUID currentUserId, UUID userId) {
        users.getUser(userId);
        var result = currentUserId.equals(userId)
                ? gifts.findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)
                : gifts.findAllByUserIdAndVisibilityAndDeletedAtIsNullOrderByCreatedAtDesc(
                        userId, GiftVisibility.PUBLIC);
        return result.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public GiftResponse update(
            UUID userId, UUID giftId, String correlationId, UpdateGiftRequest request) {
        var gift = requireOwned(userId, giftId);
        if (gift.getStatus() != GiftStatus.ACTIVE) {
            throw new BusinessException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Only active gifts can be edited");
        }
        apply(gift, request.title(), request.description(), request.link(),
                request.imageUrl(), request.estimatedPrice(), request.currency(),
                request.priority(), request.visibility());
        append("gift.updated", "GiftUpdatedEvent", gift, correlationId);
        return mapper.toResponse(gift);
    }

    @Override
    @Transactional
    public void delete(UUID userId, UUID giftId, String correlationId) {
        var gift = requireOwned(userId, giftId);
        if (gift.getStatus() == GiftStatus.RESERVED) {
            throw new BusinessException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Reserved gift cannot be deleted");
        }
        gift.setStatus(GiftStatus.ARCHIVED);
        gift.setDeletedAt(Instant.now());
        append("gift.deleted", "GiftDeletedEvent", gift, correlationId);
    }

    @Override
    @Transactional
    public GiftReservationResponse reserve(
            UUID userId, UUID giftId, String correlationId, ReserveGiftRequest request) {
        users.getUser(userId);
        var gift = gifts.findForUpdate(giftId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Gift not found"));
        if (gift.getUserId().equals(userId)) {
            throw new BusinessException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "User cannot reserve own gift");
        }
        if (gift.getStatus() != GiftStatus.ACTIVE || reservations.existsByGiftId(giftId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Gift is already reserved");
        }
        var reservation = new GiftReservation();
        reservation.setGift(gift);
        reservation.setReservedByUserId(userId);
        reservation.setComment(request.comment());
        reservation = reservations.saveAndFlush(reservation);
        gift.setStatus(GiftStatus.RESERVED);
        append("gift.reserved", "GiftReservedEvent", gift, correlationId);
        return mapper.toResponse(reservation);
    }

    private GiftWish requireOwned(UUID userId, UUID giftId) {
        var gift = gifts.findByIdAndDeletedAtIsNull(giftId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Gift not found"));
        if (!gift.getUserId().equals(userId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Gift belongs to another user");
        }
        return gift;
    }

    private void apply(
            GiftWish gift, String title, String description, String link, String imageUrl,
            java.math.BigDecimal price, String currency, GiftPriority priority,
            GiftVisibility visibility) {
        gift.setTitle(title.trim());
        gift.setDescription(description);
        gift.setLink(link);
        gift.setImageUrl(imageUrl);
        gift.setEstimatedPrice(price);
        gift.setCurrency(currency.toUpperCase(Locale.ROOT));
        gift.setPriority(priority);
        gift.setVisibility(visibility);
    }

    private void append(String topic, String type, GiftWish gift, String correlationId) {
        outbox.append(topic, type, gift.getId(), correlationId, Map.of(
                "giftId", gift.getId().toString(),
                "userId", gift.getUserId().toString(),
                "title", gift.getTitle(),
                "status", gift.getStatus().name()));
    }
}
