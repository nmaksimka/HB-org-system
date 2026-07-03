package com.example.birthday.subscriptionservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id private UUID id;
    @Column(nullable = false, length = 150) private String topic;
    @Column(name = "event_type", nullable = false, length = 150) private String eventType;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(name = "correlation_id", nullable = false, length = 100) private String correlationId;
    @Column(nullable = false, columnDefinition = "text") private String payload;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private OutboxStatus status = OutboxStatus.NEW;
    @Column(nullable = false) private int attempts;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "sent_at") private Instant sentAt;

    @PrePersist void create() { if (id == null) id = UUID.randomUUID(); createdAt = Instant.now(); }
    public UUID getId() { return id; }
    public String getTopic() { return topic; }
    public void setTopic(String value) { topic = value; }
    public String getEventType() { return eventType; }
    public void setEventType(String value) { eventType = value; }
    public UUID getAggregateId() { return aggregateId; }
    public void setAggregateId(UUID value) { aggregateId = value; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String value) { correlationId = value; }
    public String getPayload() { return payload; }
    public void setPayload(String value) { payload = value; }
    public OutboxStatus getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public Instant getCreatedAt() { return createdAt; }
    public void markSent() { status = OutboxStatus.SENT; sentAt = Instant.now(); }
    public void incrementAttempts() { attempts++; }
}
