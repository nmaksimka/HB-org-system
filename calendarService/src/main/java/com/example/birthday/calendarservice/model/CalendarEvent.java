package com.example.birthday.calendarservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity @Table(name="calendar_events")
@Getter @Setter @NoArgsConstructor
public class CalendarEvent {
    @Id private UUID id;
    @Column(name="integration_id", nullable=false) private UUID integrationId;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(name="birthday_user_id", nullable=false) private UUID birthdayUserId;
    @Column(name="external_event_id", nullable=false) private String externalEventId;
    @Column(nullable=false) private String title;
    @Column(name="event_date", nullable=false) private LocalDate eventDate;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private CalendarEventStatus status;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
    @Column(name="updated_at", nullable=false) private Instant updatedAt;
    @PrePersist void create(){
        if(id==null) id=UUID.randomUUID();
        if(status==null) status=CalendarEventStatus.ACTIVE;
        createdAt=updatedAt=Instant.now();
    }
    @PreUpdate void update(){updatedAt=Instant.now();}
}
