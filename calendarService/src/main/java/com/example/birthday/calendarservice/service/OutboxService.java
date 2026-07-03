package com.example.birthday.calendarservice.service;
import com.example.birthday.calendarservice.model.OutboxEvent;
import com.example.birthday.calendarservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Service @RequiredArgsConstructor
public class OutboxService{
    private final OutboxEventRepository events;private final ObjectMapper json;
    public void add(String topic,String type,UUID aggregate,String correlation,Object payload){
        try{var e=new OutboxEvent();e.setTopic(topic);e.setEventType(type);e.setAggregateId(aggregate);e.setCorrelationId(correlation==null?UUID.randomUUID().toString():correlation);e.setPayload(json.writeValueAsString(payload));events.save(e);}
        catch(Exception ex){throw new IllegalStateException("Cannot serialize event",ex);}
    }
}
