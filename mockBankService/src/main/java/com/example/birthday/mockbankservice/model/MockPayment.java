package com.example.birthday.mockbankservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "mock_payments")
@Getter @Setter @NoArgsConstructor
public class MockPayment {
    @Id private UUID id;
    @Column(name = "collection_id", nullable = false) private UUID collectionId;
    @Column(name = "payer_id", nullable = false) private UUID payerId;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private PaymentStatus status;
    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100) private String idempotencyKey;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    @PrePersist void create() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
}
