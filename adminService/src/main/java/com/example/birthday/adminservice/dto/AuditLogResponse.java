package com.example.birthday.adminservice.dto;
import java.time.Instant;
import java.util.UUID;
public record AuditLogResponse(UUID id,UUID eventId,String eventType,String sourceService,UUID actorId,UUID aggregateId,String correlationId,String payload,Instant occurredAt){}
