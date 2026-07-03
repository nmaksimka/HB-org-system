package com.example.birthday.chatservice.service;
import com.example.birthday.chatservice.dto.ChatMessageResponse; import com.example.birthday.contracts.event.EventEnvelope; import com.fasterxml.jackson.databind.ObjectMapper; import org.springframework.kafka.core.KafkaTemplate; import org.springframework.messaging.simp.SimpMessagingTemplate; import org.springframework.stereotype.Service; import java.time.Instant; import java.util.*;
@Service public class ChatMessageDeliveryService{
 private final SimpMessagingTemplate ws;private final KafkaTemplate<String,String> kafka;private final ObjectMapper json;
 public ChatMessageDeliveryService(SimpMessagingTemplate w,KafkaTemplate<String,String> k,ObjectMapper j){ws=w;kafka=k;json=j;}
 public void deliver(ChatMessageResponse m){ws.convertAndSend("/topic/chats/"+m.chatRoomId(),m);try{var e=new EventEnvelope<>(UUID.randomUUID(),"ChatMessageCreatedEvent","1",Instant.now(),"chat-service",UUID.randomUUID().toString(),m);kafka.send("chat.message.created",m.chatRoomId().toString(),json.writeValueAsString(e));}catch(Exception ignored){}}
}
