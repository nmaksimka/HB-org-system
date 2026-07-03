package com.example.birthday.subscriptionservice.service;

import com.example.birthday.subscriptionservice.client.*;
import com.example.birthday.subscriptionservice.exception.BusinessException;
import com.example.birthday.subscriptionservice.mapper.SubscriptionMapper;
import com.example.birthday.subscriptionservice.repository.SubscriptionRepository;
import com.example.birthday.subscriptionservice.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {
    @Mock SubscriptionRepository subscriptions;
    @Mock UserClient users;
    @Mock GroupClient groups;
    @Mock OutboxService outbox;
    private SubscriptionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SubscriptionServiceImpl(
                subscriptions, users, groups, new SubscriptionMapper(), outbox);
    }

    @Test
    void rejectsSelfSubscription() {
        UUID userId = UUID.randomUUID();
        assertThatThrownBy(() -> service.subscribeToUser(
                userId, userId, 7, "correlation"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User cannot subscribe to self");
    }

    @Test
    void rejectsDuplicateGroupSubscription() {
        UUID subscriberId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        when(subscriptions.existsBySubscriberIdAndTargetGroupIdAndActiveTrue(
                subscriberId, groupId)).thenReturn(true);
        assertThatThrownBy(() -> service.subscribeToGroup(
                subscriberId, groupId, 10, "correlation"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Active subscription already exists");
    }
}
