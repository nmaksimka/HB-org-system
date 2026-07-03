package com.example.birthday.calendarservice.dto;
import java.time.LocalDate;
import java.util.UUID;
public record CalendarEventResponse(
        UUID id, UUID integrationId, UUID birthdayUserId, String externalEventId,
        String title, LocalDate eventDate, String status) {}
