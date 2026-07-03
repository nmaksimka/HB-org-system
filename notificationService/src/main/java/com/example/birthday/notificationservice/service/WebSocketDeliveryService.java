package com.example.birthday.notificationservice.service;

import com.example.birthday.notificationservice.dto.NotificationResponse;
import com.example.birthday.notificationservice.model.DeliveryLog;
import com.example.birthday.notificationservice.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.UUID;

@Service
public class WebSocketDeliveryService {
    private final NotificationRepository notifications;
    private final DeliveryLogRepository logs;
    private final SimpMessagingTemplate messaging;

    public WebSocketDeliveryService(
            NotificationRepository notifications,
            DeliveryLogRepository logs,
            SimpMessagingTemplate messaging) {
        this.notifications = notifications;
        this.logs = logs;
        this.messaging = messaging;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deliver(UUID notificationId, NotificationResponse response) {
        var notification = notifications.findById(notificationId).orElseThrow();
        var log = new DeliveryLog();
        log.setNotification(notification);
        log.setChannel("WEBSOCKET");
        try {
            messaging.convertAndSendToUser(
                    response.userId().toString(), "/queue/notifications", response);
            notification.markSent();
            log.setStatus("DISPATCHED");
        } catch (RuntimeException ex) {
            log.setStatus("FAILED");
            log.setErrorMessage(ex.getMessage());
        }
        logs.save(log);
    }
}
