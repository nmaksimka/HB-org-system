package com.example.birthday.notificationservice.service;

import com.example.birthday.notificationservice.exception.BusinessException;
import com.example.birthday.notificationservice.mapper.NotificationMapper;
import com.example.birthday.notificationservice.repository.NotificationRepository;
import com.example.birthday.notificationservice.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Mock NotificationRepository notifications;
    @Mock WebSocketDeliveryService delivery;
    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NotificationServiceImpl(
                notifications, Mappers.getMapper(NotificationMapper.class), delivery);
    }

    @Test
    void cannotReadAnotherUsersNotification() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        when(notifications.findByIdAndUserId(notificationId, userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markRead(userId, notificationId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Notification not found");
    }
}
