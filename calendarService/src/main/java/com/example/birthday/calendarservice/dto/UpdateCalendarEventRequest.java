package com.example.birthday.calendarservice.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record UpdateCalendarEventRequest(
        @NotBlank @Size(max=255) String title, @NotNull @FutureOrPresent LocalDate eventDate) {}
