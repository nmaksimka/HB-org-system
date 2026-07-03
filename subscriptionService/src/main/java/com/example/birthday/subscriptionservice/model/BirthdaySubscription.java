package com.example.birthday.subscriptionservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "birthday_subscriptions")
@Getter
@Setter
@NoArgsConstructor
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

}
