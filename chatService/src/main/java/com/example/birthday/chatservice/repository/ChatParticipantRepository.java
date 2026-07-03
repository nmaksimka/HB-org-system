package com.example.birthday.chatservice.repository;
import com.example.birthday.chatservice.model.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,UUID>{
    boolean existsByRoomIdAndUserIdAndLeftAtIsNull(UUID roomId,UUID userId);
    List<ChatParticipant> findAllByUserIdAndLeftAtIsNull(UUID userId);
}
