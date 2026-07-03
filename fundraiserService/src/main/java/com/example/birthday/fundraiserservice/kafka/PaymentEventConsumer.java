package com.example.birthday.fundraiserservice.kafka;

import com.example.birthday.fundraiserservice.service.FundraiserService;
import com.fasterxml.jackson.databind.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final ObjectMapper json;
    private final FundraiserService service;

    @KafkaListener(topics = "payment.succeeded")
    public void consume(String message) throws Exception {
        message = message.strip();
        if (!message.isEmpty() && message.charAt(0) == '\uFEFF') message = message.substring(1);
        JsonNode root = json.readTree(message);
        if (!"PaymentSucceededEvent".equals(root.path("eventType").asText())) return;
        JsonNode payload = root.path("payload");
        service.paymentSucceeded(
                UUID.fromString(root.path("eventId").asText()),
                UUID.fromString(payload.path("paymentId").asText()),
                UUID.fromString(payload.path("fundraiserId").asText()),
                UUID.fromString(payload.path("payerId").asText()),
                new BigDecimal(payload.path("amount").asText()),
                root.path("correlationId").asText());
    }
}
