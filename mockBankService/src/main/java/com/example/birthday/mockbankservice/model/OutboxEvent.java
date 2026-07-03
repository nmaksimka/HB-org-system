package com.example.birthday.mockbankservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter @Setter @NoArgsConstructor
public class OutboxEvent {
    @Id private UUID id;
    @Column(nullable = false, length = 150) private String topic;
    @Column(name = "event_type", nullable = false, length = 150) private String eventType;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(name = "correlation_id", nullable = false, length = 100) private String correlationId;
    @Column(nullable = false, columnDefinition = "text") private String payload;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private OutboxStatus status;
    @Column(nullable = false) private int attempts;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "sent_at") private Instant sentAt;

    @PrePersist void create() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = OutboxStatus.NEW;
        createdAt = Instant.now();
    }
    public void markSent() { status = OutboxStatus.SENT; sentAt = Instant.now(); }
    public void incrementAttempts() { attempts++; }
}
