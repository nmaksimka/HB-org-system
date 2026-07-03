package com.example.birthday.mockbankservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "mock_collections")
@Getter @Setter @NoArgsConstructor
public class MockCollection {
    @Id private UUID id;
    @Column(name = "fundraiser_id", nullable = false, unique = true) private UUID fundraiserId;
    @Column(name = "owner_id", nullable = false) private UUID ownerId;
    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2) private BigDecimal targetAmount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private CollectionStatus status;
    @Column(name = "payment_url", nullable = false, length = 500) private String paymentUrl;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void create() {
        if (id == null) id = UUID.randomUUID();
        createdAt = updatedAt = Instant.now();
        if (status == null) status = CollectionStatus.ACTIVE;
    }
    @PreUpdate void update() { updatedAt = Instant.now(); }
}
