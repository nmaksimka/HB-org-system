package com.example.birthday.giftservice.kafka;

import com.example.birthday.contracts.event.EventEnvelope;
import com.example.birthday.giftservice.model.OutboxStatus;
import com.example.birthday.giftservice.repository.OutboxEventRepository;
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
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
            OutboxEventRepository events,
            KafkaTemplate<String, String> kafka,
            ObjectMapper objectMapper) {
        this.events = events;
        this.kafka = kafka;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${outbox.publish-delay-ms}")
    @Transactional
    public void publish() {
        for (var event : events.findByStatusOrderByCreatedAt(
                OutboxStatus.NEW, PageRequest.of(0, 50))) {
            try {
                JsonNode payload = objectMapper.readTree(event.getPayload());
                var envelope = new EventEnvelope<>(
                        event.getId(), event.getEventType(), "1",
                        event.getCreatedAt() == null ? Instant.now() : event.getCreatedAt(),
                        "gift-service", event.getCorrelationId(), payload);
                String json = objectMapper.writeValueAsString(envelope);
                kafka.send(event.getTopic(), event.getAggregateId().toString(), json)
                        .get(10, TimeUnit.SECONDS);
                event.markSent();
            } catch (Exception ex) {
                event.incrementAttempts();
            }
        }
    }
}
