package com.example.birthday.subscriptionservice.service;

import com.example.birthday.subscriptionservice.dto.SubscriptionResponse;

import java.util.*;

public interface SubscriptionService {
    SubscriptionResponse subscribeToUser(
            UUID subscriberId, UUID targetUserId, int daysBefore, String correlationId);
    SubscriptionResponse subscribeToGroup(
            UUID subscriberId, UUID targetGroupId, int daysBefore, String correlationId);
    List<SubscriptionResponse> mine(UUID subscriberId);
    void unsubscribe(UUID subscriberId, UUID subscriptionId, String correlationId);
}
