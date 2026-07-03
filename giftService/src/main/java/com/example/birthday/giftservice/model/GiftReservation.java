package com.example.birthday.giftservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "gift_reservations")
public class GiftReservation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gift_wish_id", nullable = false, unique = true)
    private GiftWish gift;
    @Column(name = "reserved_by_user_id", nullable = false)
    private UUID reservedByUserId;
    @Column(columnDefinition = "text")
    private String comment;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist void create() { createdAt = updatedAt = Instant.now(); }
    @PreUpdate void update() { updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public GiftWish getGift() { return gift; }
    public void setGift(GiftWish gift) { this.gift = gift; }
    public UUID getReservedByUserId() { return reservedByUserId; }
    public void setReservedByUserId(UUID value) { this.reservedByUserId = value; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Instant getCreatedAt() { return createdAt; }
}
