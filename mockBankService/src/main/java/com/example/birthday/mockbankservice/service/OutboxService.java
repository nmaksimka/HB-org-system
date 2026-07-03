package com.example.birthday.mockbankservice.service;

import com.example.birthday.mockbankservice.model.OutboxEvent;
import com.example.birthday.mockbankservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository events;
    private final ObjectMapper objectMapper;

    public void add(String topic, String eventType, UUID aggregateId, String correlationId, Object payload) {
        try {
            var event = new OutboxEvent();
            event.setTopic(topic);
            event.setEventType(eventType);
            event.setAggregateId(aggregateId);
            event.setCorrelationId(correlationId);
            event.setPayload(objectMapper.writeValueAsString(payload));
            events.save(event);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize outbox event", ex);
        }
    }
}
