package com.example.birthday.contracts.event;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        UUID eventId,
        String eventType,
        String eventVersion,
        Instant occurredAt,
        String sourceService,
        String correlationId,
        T payload
) {
}
