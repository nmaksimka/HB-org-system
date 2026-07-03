package com.example.birthday.calendarservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "calendar_integrations")
@Getter @Setter @NoArgsConstructor
public class CalendarIntegration {
    @Id private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private CalendarProvider provider;
    @Column(name="encrypted_access_token", nullable=false, columnDefinition="text") private String encryptedAccessToken;
    @Column(name="calendar_id", nullable=false) private String calendarId;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private IntegrationStatus status;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void create() {
        if (id == null) id=UUID.randomUUID();
        if (status == null) status=IntegrationStatus.ACTIVE;
        createdAt=updatedAt=Instant.now();
    }
    @PreUpdate void update(){updatedAt=Instant.now();}
}
