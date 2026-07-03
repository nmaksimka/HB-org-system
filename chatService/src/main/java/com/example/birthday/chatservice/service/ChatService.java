package com.example.birthday.chatservice.service;
import com.example.birthday.chatservice.dto.*; import org.springframework.data.domain.*; import java.util.*;
public interface ChatService{
 ChatRoomResponse createBirthday(UUID userId,UUID targetId,CreateChatRequest request);
 ChatRoomResponse join(UUID userId,UUID roomId);
 List<ChatRoomResponse> mine(UUID userId);
 Page<ChatMessageResponse> messages(UUID userId,UUID roomId,Pageable pageable);
 ChatMessageResponse send(UUID userId,UUID roomId,SendMessageRequest request);
 void assertAccess(UUID userId,UUID roomId);
}
