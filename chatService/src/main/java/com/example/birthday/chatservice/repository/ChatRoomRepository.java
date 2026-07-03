package com.example.birthday.chatservice.repository;
import com.example.birthday.chatservice.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ChatRoomRepository extends JpaRepository<ChatRoom,UUID>{
    Optional<ChatRoom> findByIdAndDeletedAtIsNull(UUID id);
}
