package com.example.birthday.groupservice.mapper;

import com.example.birthday.groupservice.dto.GroupResponse;
import com.example.birthday.groupservice.model.BirthdayGroup;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {
    public GroupResponse toResponse(BirthdayGroup group) {
        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getOwnerId(),
                group.getAvatarUrl(),
                group.isPublicGroup(),
                group.getCreatedAt()
        );
    }
}
