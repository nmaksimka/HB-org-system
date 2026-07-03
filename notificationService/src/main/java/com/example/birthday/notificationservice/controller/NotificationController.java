package com.example.birthday.notificationservice.controller;

import com.example.birthday.notificationservice.dto.*;
import com.example.birthday.notificationservice.model.NotificationStatus;
import com.example.birthday.notificationservice.service.NotificationService;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notifications;
    public NotificationController(NotificationService notifications) {
        this.notifications = notifications;
    }

    @GetMapping
    public Page<NotificationResponse> list(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) NotificationStatus status,
            Pageable pageable) {
        return notifications.list(userId, status, pageable);
    }

    @PatchMapping("/{notificationId}/read")
    public NotificationResponse read(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID notificationId) {
        return notifications.markRead(userId, notificationId);
    }

    @PatchMapping("/read-all")
    public ReadAllResponse readAll(@RequestHeader("X-User-Id") UUID userId) {
        return new ReadAllResponse(notifications.markAllRead(userId));
    }
}
