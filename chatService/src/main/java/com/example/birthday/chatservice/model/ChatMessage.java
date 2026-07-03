package com.example.birthday.chatservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="chat_messages")
public class ChatMessage {
    @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="chat_room_id") private ChatRoom room;
    @Column(name="sender_id",nullable=false) private UUID senderId;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private MessageType type=MessageType.TEXT;
    @Column(nullable=false,columnDefinition="text") private String content;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @Column(name="deleted_at") private Instant deletedAt;
    @PrePersist void create(){createdAt=Instant.now();}
    public UUID getId(){return id;} public ChatRoom getRoom(){return room;}
    public void setRoom(ChatRoom v){room=v;} public UUID getSenderId(){return senderId;}
    public void setSenderId(UUID v){senderId=v;} public MessageType getType(){return type;}
    public void setType(MessageType v){type=v;} public String getContent(){return content;}
    public void setContent(String v){content=v;} public Instant getCreatedAt(){return createdAt;}
}
