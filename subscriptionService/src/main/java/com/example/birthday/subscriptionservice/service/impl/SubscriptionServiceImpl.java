package com.example.birthday.subscriptionservice.service.impl;

import com.example.birthday.subscriptionservice.client.*;
import com.example.birthday.subscriptionservice.dto.SubscriptionResponse;
import com.example.birthday.subscriptionservice.exception.BusinessException;
import com.example.birthday.subscriptionservice.mapper.SubscriptionMapper;
import com.example.birthday.subscriptionservice.model.*;
import com.example.birthday.subscriptionservice.repository.SubscriptionRepository;
import com.example.birthday.subscriptionservice.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptions;
    private final UserClient users;
    private final GroupClient groups;
    private final SubscriptionMapper mapper;
    private final OutboxService outbox;

    public SubscriptionServiceImpl(
            SubscriptionRepository subscriptions, UserClient users, GroupClient groups,
            SubscriptionMapper mapper, OutboxService outbox) {
        this.subscriptions = subscriptions;
        this.users = users;
        this.groups = groups;
        this.mapper = mapper;
        this.outbox = outbox;
    }

    @Override
    @Transactional
    public SubscriptionResponse subscribeToUser(
            UUID subscriberId, UUID targetId, int daysBefore, String correlationId) {
        if (subscriberId.equals(targetId)) {
            throw new BusinessException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "User cannot subscribe to self");
        }
        if (subscriptions.existsBySubscriberIdAndTargetUserIdAndActiveTrue(
                subscriberId, targetId)) {
            throw duplicate();
        }
        users.get(targetId);
        var value = base(subscriberId, SubscriptionType.USER, daysBefore);
        value.setTargetUserId(targetId);
        value = subscriptions.saveAndFlush(value);
        created(value, targetId, correlationId);
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public SubscriptionResponse subscribeToGroup(
            UUID subscriberId, UUID targetId, int daysBefore, String correlationId) {
        if (subscriptions.existsBySubscriberIdAndTargetGroupIdAndActiveTrue(
                subscriberId, targetId)) {
            throw duplicate();
        }
        groups.get(targetId);
        var value = base(subscriberId, SubscriptionType.GROUP, daysBefore);
        value.setTargetGroupId(targetId);
        value = subscriptions.saveAndFlush(value);
        created(value, targetId, correlationId);
        return mapper.toResponse(value);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> mine(UUID subscriberId) {
        return subscriptions.findAllBySubscriberIdAndActiveTrueOrderByCreatedAtDesc(
                subscriberId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void unsubscribe(UUID subscriberId, UUID subscriptionId, String correlationId) {
        var value = subscriptions.findByIdAndSubscriberIdAndActiveTrue(
                        subscriptionId, subscriberId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND, "Active subscription not found"));
        value.setActive(false);
        outbox.append("subscription.deleted", "SubscriptionDeletedEvent",
                value.getId(), correlationId, Map.of(
                        "subscriptionId", value.getId().toString(),
                        "subscriberId", subscriberId.toString(),
                        "subscriptionType", value.getType().name()));
    }

    private BirthdaySubscription base(
            UUID subscriberId, SubscriptionType type, int daysBefore) {
        var value = new BirthdaySubscription();
        value.setSubscriberId(subscriberId);
        value.setType(type);
        value.setDaysBefore(daysBefore);
        return value;
    }

    private void created(BirthdaySubscription value, UUID targetId, String correlationId) {
        outbox.append("subscription.created", "SubscriptionCreatedEvent",
                value.getId(), correlationId, Map.of(
                        "subscriptionId", value.getId().toString(),
                        "subscriberId", value.getSubscriberId().toString(),
                        "subscriptionType", value.getType().name(),
                        "targetId", targetId.toString(),
                        "daysBefore", value.getDaysBefore()));
    }

    private BusinessException duplicate() {
        return new BusinessException(HttpStatus.CONFLICT, "Active subscription already exists");
    }
}
