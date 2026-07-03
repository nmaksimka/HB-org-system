package com.example.birthday.notificationservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_delivery_logs")
public class DeliveryLog {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false) private Notification notification;
    @Column(nullable = false, length = 30) private String channel;
    @Column(nullable = false, length = 30) private String status;
    @Column(name = "error_message", columnDefinition = "text") private String errorMessage;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @PrePersist void create() { createdAt = Instant.now(); }
    public void setNotification(Notification value) { notification = value; }
    public void setChannel(String value) { channel = value; }
    public void setStatus(String value) { status = value; }
    public void setErrorMessage(String value) { errorMessage = value; }
}
