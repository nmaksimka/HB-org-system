package com.example.birthday.calendarservice.dto;
import com.example.birthday.calendarservice.model.CalendarProvider;
import jakarta.validation.constraints.*;
public record ConnectCalendarRequest(
        @NotNull CalendarProvider provider,
        @NotBlank String accessToken,
        @NotBlank @Size(max=255) String calendarId) {}
