package com.example.birthday.chatservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "chat_rooms")
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private ChatRoomType type;
    @Column(nullable=false,length=255) private String title;
    @Column(name="target_user_id") private UUID targetUserId;
    @Column(name="target_gift_id") private UUID targetGiftId;
    @Column(name="group_id") private UUID groupId;
    @Column(name="created_by_user_id",nullable=false) private UUID createdByUserId;
    @Column(name="is_active",nullable=false) private boolean active=true;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    @Column(name="deleted_at") private Instant deletedAt;
    @PrePersist void create(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void update(){updatedAt=Instant.now();}
    public UUID getId(){return id;} public ChatRoomType getType(){return type;}
    public void setType(ChatRoomType v){type=v;} public String getTitle(){return title;}
    public void setTitle(String v){title=v;} public UUID getTargetUserId(){return targetUserId;}
    public void setTargetUserId(UUID v){targetUserId=v;} public UUID getCreatedByUserId(){return createdByUserId;}
    public void setCreatedByUserId(UUID v){createdByUserId=v;} public Instant getCreatedAt(){return createdAt;}
}
