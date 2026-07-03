package com.example.birthday.notificationservice.service.impl;

import com.example.birthday.notificationservice.dto.NotificationResponse;
import com.example.birthday.notificationservice.exception.BusinessException;
import com.example.birthday.notificationservice.mapper.NotificationMapper;
import com.example.birthday.notificationservice.model.*;
import com.example.birthday.notificationservice.repository.NotificationRepository;
import com.example.birthday.notificationservice.service.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.*;

import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifications;
    private final NotificationMapper mapper;
    private final WebSocketDeliveryService delivery;

    public NotificationServiceImpl(
            NotificationRepository notifications,
            NotificationMapper mapper,
            WebSocketDeliveryService delivery) {
        this.notifications = notifications;
        this.mapper = mapper;
        this.delivery = delivery;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> list(
            UUID userId, NotificationStatus status, Pageable pageable) {
        var page = status == null
                ? notifications.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                : notifications.findAllByUserIdAndStatusOrderByCreatedAtDesc(
                        userId, status, pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional
    public NotificationResponse markRead(UUID userId, UUID notificationId) {
        var value = notifications.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND, "Notification not found"));
        value.markRead();
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public int markAllRead(UUID userId) {
        var values = notifications.findAllByUserIdAndStatus(
                userId, NotificationStatus.UNREAD);
        values.forEach(Notification::markRead);
        return values.size();
    }

    @Override
    @Transactional
    public void createFromEvent(
            UUID eventId, UUID userId, NotificationType type, String title, String message,
            String relatedType, UUID relatedId) {
        if (notifications.existsBySourceEventId(eventId)) {
            return;
        }
        var value = new Notification();
        value.setSourceEventId(eventId);
        value.setUserId(userId);
        value.setType(type);
        value.setTitle(title);
        value.setMessage(message);
        value.setRelatedEntityType(relatedType);
        value.setRelatedEntityId(relatedId);
        try {
            value = notifications.saveAndFlush(value);
        } catch (DataIntegrityViolationException duplicate) {
            return;
        }
        NotificationResponse response = mapper.toResponse(value);
        UUID id = value.getId();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override public void afterCommit() {
                            delivery.deliver(id, response);
                        }
                    });
        }
    }
}
