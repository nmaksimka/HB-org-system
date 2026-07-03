package com.example.birthday.adminservice.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="audit_logs") @Getter @Setter @NoArgsConstructor
public class AuditLog{
 @Id private UUID id;@Column(name="event_id",unique=true)private UUID eventId;@Column(name="event_type",nullable=false)private String eventType;
 @Column(name="source_service",nullable=false)private String sourceService;@Column(name="actor_id")private UUID actorId;@Column(name="aggregate_id")private UUID aggregateId;
 @Column(name="correlation_id")private String correlationId;@Column(nullable=false,columnDefinition="text")private String payload;
 @Column(name="occurred_at",nullable=false)private Instant occurredAt;@Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;
 @PrePersist void create(){if(id==null)id=UUID.randomUUID();createdAt=Instant.now();}
}
