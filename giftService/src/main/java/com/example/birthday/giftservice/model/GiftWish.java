package com.example.birthday.giftservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "gift_wishes")
@Getter
@Setter
@NoArgsConstructor
public class GiftWish {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String link;
    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;
    @Column(name = "estimated_price", precision = 12, scale = 2)
    private BigDecimal estimatedPrice;
    @Column(nullable = false, length = 3)
    private String currency = "RUB";
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private GiftPriority priority = GiftPriority.MEDIUM;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private GiftVisibility visibility = GiftVisibility.PUBLIC;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private GiftStatus status = GiftStatus.ACTIVE;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist void create() { createdAt = updatedAt = Instant.now(); }
    @PreUpdate void update() { updatedAt = Instant.now(); }

}
