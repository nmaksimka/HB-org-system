package com.example.birthday.chatservice.mapper;

import com.example.birthday.chatservice.dto.*;
import com.example.birthday.chatservice.model.*;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {
    ChatRoomResponse toResponse(ChatRoom value);

    @Mapping(target = "chatRoomId", source = "room.id")
    ChatMessageResponse toResponse(ChatMessage value);
}
