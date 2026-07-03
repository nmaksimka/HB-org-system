package com.example.birthday.chatservice.repository;
import com.example.birthday.chatservice.model.ChatMessage;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ChatMessageRepository extends JpaRepository<ChatMessage,UUID>{
    Page<ChatMessage> findAllByRoomIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID roomId,Pageable pageable);
}
