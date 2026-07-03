package com.example.birthday.groupservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class BirthdayGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "is_public", nullable = false)
    private boolean publicGroup = true;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    void create() { createdAt = updatedAt = Instant.now(); }
    @PreUpdate
    void update() { updatedAt = Instant.now(); }

}
