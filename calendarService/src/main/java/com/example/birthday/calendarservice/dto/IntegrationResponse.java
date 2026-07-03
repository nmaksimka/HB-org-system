package com.example.birthday.calendarservice.dto;
import java.time.Instant;
import java.util.UUID;
public record IntegrationResponse(
        UUID id, UUID userId, String provider, String calendarId, String status, Instant createdAt) {}
