package com.example.birthday.calendarservice.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;
public record CreateCalendarEventRequest(
        @NotNull UUID integrationId, @NotNull UUID birthdayUserId,
        @NotBlank @Size(max=255) String title, @NotNull @FutureOrPresent LocalDate eventDate) {}
