package com.example.birthday.subscriptionservice.service;

import com.example.birthday.subscriptionservice.model.OutboxEvent;
import com.example.birthday.subscriptionservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxEventRepository events;
    private final ObjectMapper json;
    public OutboxService(OutboxEventRepository events, ObjectMapper json) {
        this.events = events;
        this.json = json;
    }
    public void append(
            String topic, String type, UUID aggregateId, String correlationId,
            Map<String, Object> payload) {
        var event = new OutboxEvent();
        event.setTopic(topic);
        event.setEventType(type);
        event.setAggregateId(aggregateId);
        event.setCorrelationId(correlationId);
        try {
            event.setPayload(json.writeValueAsString(payload));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot serialize outbox payload", ex);
        }
        events.save(event);
    }
}
