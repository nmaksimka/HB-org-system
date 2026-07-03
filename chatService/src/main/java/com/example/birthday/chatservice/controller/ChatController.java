package com.example.birthday.chatservice.controller;
import com.example.birthday.chatservice.dto.*; import com.example.birthday.chatservice.service.ChatService; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/chats") public class ChatController{
 private final ChatService chats;public ChatController(ChatService c){chats=c;}
 @PostMapping("/birthday/{targetUserId}")@ResponseStatus(HttpStatus.CREATED)public ChatRoomResponse create(@RequestHeader("X-User-Id")UUID u,@PathVariable UUID targetUserId,@Valid @RequestBody CreateChatRequest q){return chats.createBirthday(u,targetUserId,q);}
 @PostMapping("/{roomId}/join")public ChatRoomResponse join(@RequestHeader("X-User-Id")UUID u,@PathVariable UUID roomId){return chats.join(u,roomId);}
 @GetMapping("/me")public List<ChatRoomResponse> mine(@RequestHeader("X-User-Id")UUID u){return chats.mine(u);}
 @GetMapping("/{roomId}/messages")public Page<ChatMessageResponse> messages(@RequestHeader("X-User-Id")UUID u,@PathVariable UUID roomId,Pageable p){return chats.messages(u,roomId,p);}
 @PostMapping("/{roomId}/messages")@ResponseStatus(HttpStatus.CREATED)public ChatMessageResponse send(@RequestHeader("X-User-Id")UUID u,@PathVariable UUID roomId,@Valid @RequestBody SendMessageRequest q){return chats.send(u,roomId,q);}
}
