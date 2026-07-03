package com.example.birthday.subscriptionservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "birthday_subscriptions")
public class BirthdaySubscription {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "subscriber_id", nullable = false)
    private UUID subscriberId;
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 20)
    private SubscriptionType type;
    @Column(name = "target_user_id")
    private UUID targetUserId;
    @Column(name = "target_group_id")
    private UUID targetGroupId;
    @Column(name = "days_before", nullable = false)
    private int daysBefore;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist void create() { createdAt = updatedAt = Instant.now(); }
    @PreUpdate void update() { updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public UUID getSubscriberId() { return subscriberId; }
    public void setSubscriberId(UUID value) { subscriberId = value; }
    public SubscriptionType getType() { return type; }
    public void setType(SubscriptionType value) { type = value; }
    public UUID getTargetUserId() { return targetUserId; }
    public void setTargetUserId(UUID value) { targetUserId = value; }
    public UUID getTargetGroupId() { return targetGroupId; }
    public void setTargetGroupId(UUID value) { targetGroupId = value; }
    public int getDaysBefore() { return daysBefore; }
    public void setDaysBefore(int value) { daysBefore = value; }
    public boolean isActive() { return active; }
    public void setActive(boolean value) { active = value; }
    public Instant getCreatedAt() { return createdAt; }
}
