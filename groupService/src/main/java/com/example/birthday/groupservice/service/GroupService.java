package com.example.birthday.groupservice.service;

import com.example.birthday.groupservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    GroupResponse create(UUID currentUserId, CreateGroupRequest request);
    Page<GroupResponse> list(Pageable pageable);
    GroupResponse get(UUID currentUserId, UUID groupId);
    GroupResponse join(UUID currentUserId, UUID groupId);
    void leave(UUID currentUserId, UUID groupId);
    List<GroupMemberResponse> members(UUID currentUserId, UUID groupId);
}
