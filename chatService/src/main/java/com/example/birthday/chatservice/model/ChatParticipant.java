package com.example.birthday.chatservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="chat_participants")
public class ChatParticipant {
    @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="chat_room_id") private ChatRoom room;
    @Column(name="user_id",nullable=false) private UUID userId;
    @Column(name="joined_at",nullable=false,updatable=false) private Instant joinedAt;
    @Column(name="left_at") private Instant leftAt;
    @PrePersist void create(){joinedAt=Instant.now();}
    public void setRoom(ChatRoom v){room=v;} public ChatRoom getRoom(){return room;}
    public void setUserId(UUID v){userId=v;} public UUID getUserId(){return userId;}
}
