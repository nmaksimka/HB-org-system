package com.example.birthday.chatservice.dto;
import java.time.Instant; import java.util.UUID;
public record ChatMessageResponse(UUID id,UUID chatRoomId,UUID senderId,String type,String content,Instant createdAt){}
