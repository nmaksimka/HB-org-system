package com.example.birthday.chatservice.dto;
import java.time.Instant; import java.util.UUID;
public record ChatRoomResponse(UUID id,String type,String title,UUID targetUserId,UUID createdByUserId,Instant createdAt){}
