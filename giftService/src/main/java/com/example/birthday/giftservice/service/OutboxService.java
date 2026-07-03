package com.example.birthday.giftservice.service;

import com.example.birthday.giftservice.model.OutboxEvent;
import com.example.birthday.giftservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxEventRepository events;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxEventRepository events, ObjectMapper objectMapper) {
        this.events = events;
        this.objectMapper = objectMapper;
    }

    public void append(
            String topic,
            String eventType,
            UUID aggregateId,
            String correlationId,
            Map<String, Object> payload) {
        var event = new OutboxEvent();
        event.setTopic(topic);
        event.setEventType(eventType);
        event.setAggregateId(aggregateId);
        event.setCorrelationId(correlationId);
        try {
            event.setPayload(objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize outbox payload", ex);
        }
        events.save(event);
    }
}
