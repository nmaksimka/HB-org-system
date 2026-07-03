package com.example.birthday.notificationservice.service;

import com.example.birthday.notificationservice.dto.NotificationResponse;
import com.example.birthday.notificationservice.model.*;
import org.springframework.data.domain.*;

import java.util.UUID;

public interface NotificationService {
    Page<NotificationResponse> list(UUID userId, NotificationStatus status, Pageable pageable);
    NotificationResponse markRead(UUID userId, UUID notificationId);
    int markAllRead(UUID userId);
    void createFromEvent(
            UUID eventId, UUID userId, NotificationType type, String title, String message,
            String relatedType, UUID relatedId);
}
