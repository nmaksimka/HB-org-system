package com.example.birthday.subscriptionservice.controller;

import com.example.birthday.subscriptionservice.dto.*;
import com.example.birthday.subscriptionservice.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptions;
    public SubscriptionController(SubscriptionService subscriptions) {
        this.subscriptions = subscriptions;
    }

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse user(
            @RequestHeader("X-User-Id") UUID subscriberId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID userId,
            @Valid @RequestBody CreateSubscriptionRequest request) {
        return subscriptions.subscribeToUser(
                subscriberId, userId, request.daysBefore(), correlationId);
    }

    @PostMapping("/groups/{groupId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse group(
            @RequestHeader("X-User-Id") UUID subscriberId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID groupId,
            @Valid @RequestBody CreateSubscriptionRequest request) {
        return subscriptions.subscribeToGroup(
                subscriberId, groupId, request.daysBefore(), correlationId);
    }

    @GetMapping("/me")
    public List<SubscriptionResponse> mine(
            @RequestHeader("X-User-Id") UUID subscriberId) {
        return subscriptions.mine(subscriberId);
    }

    @DeleteMapping("/{subscriptionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader("X-User-Id") UUID subscriberId,
            @RequestHeader("X-Correlation-Id") String correlationId,
            @PathVariable UUID subscriptionId) {
        subscriptions.unsubscribe(subscriberId, subscriptionId, correlationId);
    }
}
