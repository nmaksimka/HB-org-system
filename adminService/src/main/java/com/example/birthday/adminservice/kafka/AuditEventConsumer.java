package com.example.birthday.adminservice.kafka;
import com.example.birthday.adminservice.service.AdminService;import com.fasterxml.jackson.databind.*;import lombok.RequiredArgsConstructor;import org.springframework.kafka.annotation.KafkaListener;import org.springframework.stereotype.Component;import java.time.Instant;import java.util.UUID;
@Component @RequiredArgsConstructor public class AuditEventConsumer{
 private final ObjectMapper json;private final AdminService service;
 @KafkaListener(topicPattern="^(user|group|gift|subscription|notification|chat|fundraiser|payment|calendar)\\..+$")
 public void consume(String message)throws Exception{message=message.strip();if(!message.isEmpty()&&message.charAt(0)=='\uFEFF')message=message.substring(1);JsonNode r=json.readTree(message);service.record(UUID.fromString(r.path("eventId").asText()),r.path("eventType").asText(),r.path("sourceService").asText(),r.path("correlationId").asText(),r.path("payload").toString(),Instant.parse(r.path("occurredAt").asText()));}
}
