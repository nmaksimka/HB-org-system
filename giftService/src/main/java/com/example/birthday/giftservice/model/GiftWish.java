package com.example.birthday.giftservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "gift_wishes")
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

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(BigDecimal value) { this.estimatedPrice = value; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public GiftPriority getPriority() { return priority; }
    public void setPriority(GiftPriority priority) { this.priority = priority; }
    public GiftVisibility getVisibility() { return visibility; }
    public void setVisibility(GiftVisibility visibility) { this.visibility = visibility; }
    public GiftStatus getStatus() { return status; }
    public void setStatus(GiftStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
