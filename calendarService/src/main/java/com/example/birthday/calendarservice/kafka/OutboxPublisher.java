package com.example.birthday.calendarservice.kafka;
import com.example.birthday.calendarservice.model.OutboxStatus;
import com.example.birthday.calendarservice.repository.OutboxEventRepository;
import com.example.birthday.contracts.event.EventEnvelope;
import com.fasterxml.jackson.databind.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
@Component @RequiredArgsConstructor
public class OutboxPublisher{
    private final OutboxEventRepository events;private final KafkaTemplate<String,String> kafka;private final ObjectMapper json;
    @Scheduled(fixedDelayString="${outbox.publish-delay-ms}") @Transactional public void publish(){
        for(var e:events.findByStatusOrderByCreatedAt(OutboxStatus.NEW,PageRequest.of(0,50)))try{
            var envelope=new EventEnvelope<>(e.getId(),e.getEventType(),"1",e.getCreatedAt()==null?Instant.now():e.getCreatedAt(),"calendar-service",e.getCorrelationId(),json.readTree(e.getPayload()));
            kafka.send(e.getTopic(),e.getAggregateId().toString(),json.writeValueAsString(envelope)).get(10,TimeUnit.SECONDS);e.markSent();
        }catch(Exception ex){e.incrementAttempts();}
    }
}
