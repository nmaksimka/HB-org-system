package com.example.birthday.chatservice.mapper;
import com.example.birthday.chatservice.dto.*; import com.example.birthday.chatservice.model.*; import org.springframework.stereotype.Component;
@Component public class ChatMapper{
 public ChatRoomResponse toResponse(ChatRoom v){return new ChatRoomResponse(v.getId(),v.getType().name(),v.getTitle(),v.getTargetUserId(),v.getCreatedByUserId(),v.getCreatedAt());}
 public ChatMessageResponse toResponse(ChatMessage v){return new ChatMessageResponse(v.getId(),v.getRoom().getId(),v.getSenderId(),v.getType().name(),v.getContent(),v.getCreatedAt());}
}
