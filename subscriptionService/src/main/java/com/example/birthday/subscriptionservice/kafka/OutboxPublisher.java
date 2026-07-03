package com.example.birthday.subscriptionservice.kafka;

import com.example.birthday.contracts.event.EventEnvelope;
import com.example.birthday.subscriptionservice.model.OutboxStatus;
import com.example.birthday.subscriptionservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxPublisher {
    private final OutboxEventRepository events;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper json;
    public OutboxPublisher(
            OutboxEventRepository events, KafkaTemplate<String, String> kafka, ObjectMapper json) {
        this.events = events;
        this.kafka = kafka;
        this.json = json;
    }
    @Scheduled(fixedDelayString = "${outbox.publish-delay-ms}")
    @Transactional
    public void publish() {
        for (var event : events.findByStatusOrderByCreatedAt(
                OutboxStatus.NEW, PageRequest.of(0, 50))) {
            try {
                var envelope = new EventEnvelope<JsonNode>(
                        event.getId(), event.getEventType(), "1",
                        event.getCreatedAt() == null ? Instant.now() : event.getCreatedAt(),
                        "subscription-service", event.getCorrelationId(),
                        json.readTree(event.getPayload()));
                kafka.send(event.getTopic(), event.getAggregateId().toString(),
                        json.writeValueAsString(envelope)).get(10, TimeUnit.SECONDS);
                event.markSent();
            } catch (Exception ex) {
                event.incrementAttempts();
            }
        }
    }
}
