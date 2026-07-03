package com.example.birthday.notificationservice.kafka;

import com.example.birthday.notificationservice.model.NotificationType;
import com.example.birthday.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationEventConsumerTest {
    @Mock NotificationService notifications;

    @Test
    void mapsBirthdayReminderEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID subscriberId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        String message = """
                {
                  "eventId":"%s",
                  "eventType":"BirthdayReminderRequestedEvent",
                  "payload":{
                    "subscriberId":"%s",
                    "targetUserId":"%s",
                    "targetName":"Максим",
                    "daysBefore":7
                  }
                }
                """.formatted(eventId, subscriberId, targetId);

        new NotificationEventConsumer(new ObjectMapper(), notifications).consume("\uFEFF" + message);

        verify(notifications).createFromEvent(
                eventId, subscriberId, NotificationType.BIRTHDAY_REMINDER,
                "Скоро день рождения",
                "До дня рождения Максим осталось дней: 7",
                "USER", targetId);
    }
}
