package com.example.birthday.fundraiserservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fundraiser_participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fundraiser_id", "user_id"}))
@Getter @Setter @NoArgsConstructor
public class FundraiserParticipant {
    @Id private UUID id;
    @Column(name = "fundraiser_id", nullable = false) private UUID fundraiserId;
    @Column(name = "user_id", nullable = false) private UUID userId;
    @Column(name = "contributed_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal contributedAmount;
    @Column(name = "joined_at", nullable = false, updatable = false) private Instant joinedAt;

    @PrePersist void create() {
        if (id == null) id = UUID.randomUUID();
        if (contributedAmount == null) contributedAmount = BigDecimal.ZERO;
        joinedAt = Instant.now();
    }
}
