package com.example.birthday.fundraiserservice.kafka;

import com.example.birthday.contracts.event.EventEnvelope;
import com.example.birthday.fundraiserservice.model.OutboxStatus;
import com.example.birthday.fundraiserservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxEventRepository events;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper objectMapper;
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
                        "fundraiser-service", event.getCorrelationId(), payload);
                kafka.send(event.getTopic(), event.getAggregateId().toString(),
                        objectMapper.writeValueAsString(envelope)).get(10, TimeUnit.SECONDS);
                event.markSent();
            } catch (Exception ex) {
                event.incrementAttempts();
            }
        }
    }
}
