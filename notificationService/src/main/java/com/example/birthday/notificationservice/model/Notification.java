package com.example.birthday.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false) private UUID userId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 50)
    private NotificationType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.UNREAD;
    @Column(nullable = false, length = 255) private String title;
    @Column(nullable = false, columnDefinition = "text") private String message;
    @Column(name = "related_entity_type", length = 50) private String relatedEntityType;
    @Column(name = "related_entity_id") private UUID relatedEntityId;
    @Column(name = "source_event_id", nullable = false, unique = true) private UUID sourceEventId;
    @Column(name = "scheduled_for") private Instant scheduledFor;
    @Column(name = "sent_at") private Instant sentAt;
    @Column(name = "read_at") private Instant readAt;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    @PrePersist void create() { createdAt = Instant.now(); }
    public void markSent() { sentAt = Instant.now(); }
    public void markRead() { status = NotificationStatus.READ; readAt = Instant.now(); }
}
