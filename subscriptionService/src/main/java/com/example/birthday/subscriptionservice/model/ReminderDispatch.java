package com.example.birthday.subscriptionservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reminder_dispatches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subscription_id", "birthday_year"}))
public class ReminderDispatch {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionId;
    @Column(name = "birthday_year", nullable = false)
    private int birthdayYear;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @PrePersist void create() { createdAt = Instant.now(); }
    public void setSubscriptionId(UUID value) { subscriptionId = value; }
    public void setBirthdayYear(int value) { birthdayYear = value; }
}
