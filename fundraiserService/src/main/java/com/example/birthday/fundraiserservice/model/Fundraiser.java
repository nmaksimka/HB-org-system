package com.example.birthday.fundraiserservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fundraisers")
@Getter @Setter @NoArgsConstructor
public class Fundraiser {
    @Id private UUID id;
    @Column(name = "owner_id", nullable = false) private UUID ownerId;
    @Column(name = "beneficiary_id", nullable = false) private UUID beneficiaryId;
    @Column(name = "gift_id", nullable = false) private UUID giftId;
    @Column(nullable = false, length = 200) private String title;
    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2) private BigDecimal targetAmount;
    @Column(name = "collected_amount", nullable = false, precision = 19, scale = 2) private BigDecimal collectedAmount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private FundraiserStatus status;
    @Column(name = "payment_url", length = 500) private String paymentUrl;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void create() {
        if (id == null) id = UUID.randomUUID();
        if (collectedAmount == null) collectedAmount = BigDecimal.ZERO;
        if (status == null) status = FundraiserStatus.DRAFT;
        createdAt = updatedAt = Instant.now();
    }
    @PreUpdate void update() { updatedAt = Instant.now(); }
}
