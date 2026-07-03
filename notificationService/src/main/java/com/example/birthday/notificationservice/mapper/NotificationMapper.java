package com.example.birthday.notificationservice.mapper;

import com.example.birthday.notificationservice.dto.NotificationResponse;
import com.example.birthday.notificationservice.model.Notification;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    NotificationResponse toResponse(Notification value);
}
