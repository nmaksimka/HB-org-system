package com.example.birthday.notificationservice.kafka;

import com.example.birthday.notificationservice.model.NotificationType;
import com.example.birthday.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationEventConsumer {
    private final ObjectMapper json;
    private final NotificationService notifications;

    public NotificationEventConsumer(ObjectMapper json, NotificationService notifications) {
        this.json = json;
        this.notifications = notifications;
    }

    @KafkaListener(topics = {"subscription.created", "birthday.reminder.requested"})
    public void consume(String message) throws Exception {
        message = message.strip();
        if (!message.isEmpty() && message.charAt(0) == '\uFEFF') {
            message = message.substring(1);
        }
        JsonNode root = json.readTree(message);
        UUID eventId = UUID.fromString(root.path("eventId").asText());
        String eventType = root.path("eventType").asText();
        JsonNode payload = root.path("payload");
        if ("SubscriptionCreatedEvent".equals(eventType)) {
            UUID userId = uuid(payload, "subscriberId");
            UUID subscriptionId = uuid(payload, "subscriptionId");
            notifications.createFromEvent(
                    eventId, userId, NotificationType.SYSTEM,
                    "Подписка создана",
                    "Напоминания о дне рождения успешно включены.",
                    "SUBSCRIPTION", subscriptionId);
        } else if ("BirthdayReminderRequestedEvent".equals(eventType)) {
            UUID userId = uuid(payload, "subscriberId");
            UUID targetUserId = uuid(payload, "targetUserId");
            int days = payload.path("daysBefore").asInt();
            String name = payload.path("targetName").asText("пользователя");
            notifications.createFromEvent(
                    eventId, userId, NotificationType.BIRTHDAY_REMINDER,
                    "Скоро день рождения",
                    "До дня рождения " + name + " осталось дней: " + days,
                    "USER", targetUserId);
        }
    }

    private UUID uuid(JsonNode node, String field) {
        return UUID.fromString(node.path(field).asText());
    }
}
