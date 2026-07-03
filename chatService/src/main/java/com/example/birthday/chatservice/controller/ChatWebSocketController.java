package com.example.birthday.chatservice.controller;
import com.example.birthday.chatservice.dto.*; import com.example.birthday.chatservice.service.ChatService; import jakarta.validation.Valid; import org.springframework.messaging.handler.annotation.*; import org.springframework.stereotype.Controller; import java.security.Principal; import java.util.UUID;
@Controller public class ChatWebSocketController{
 private final ChatService chats;public ChatWebSocketController(ChatService c){chats=c;}
 @MessageMapping("/chats/{roomId}/send")public void send(@DestinationVariable UUID roomId,@Valid SendMessageRequest q,Principal p){chats.send(UUID.fromString(p.getName()),roomId,q);}
}
