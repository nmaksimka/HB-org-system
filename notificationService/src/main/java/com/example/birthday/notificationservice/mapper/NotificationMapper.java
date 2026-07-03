package com.example.birthday.notificationservice.mapper;

import com.example.birthday.notificationservice.dto.NotificationResponse;
import com.example.birthday.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(Notification value) {
        return new NotificationResponse(
                value.getId(), value.getUserId(), value.getType().name(),
                value.getStatus().name(), value.getTitle(), value.getMessage(),
                value.getRelatedEntityType(), value.getRelatedEntityId(),
                value.getSentAt(), value.getCreatedAt());
    }
}
