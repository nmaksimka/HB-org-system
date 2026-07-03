package com.example.birthday.groupservice.controller;

import com.example.birthday.groupservice.dto.*;
import com.example.birthday.groupservice.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groups;

    public GroupController(GroupService groups) {
        this.groups = groups;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse create(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @Valid @RequestBody CreateGroupRequest request) {
        return groups.create(currentUserId, request);
    }

    @GetMapping
    public Page<GroupResponse> list(Pageable pageable) {
        return groups.list(pageable);
    }

    @GetMapping("/{groupId}")
    public GroupResponse get(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @PathVariable UUID groupId) {
        return groups.get(currentUserId, groupId);
    }

    @PostMapping("/{groupId}/join")
    public GroupResponse join(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @PathVariable UUID groupId) {
        return groups.join(currentUserId, groupId);
    }

    @DeleteMapping("/{groupId}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leave(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @PathVariable UUID groupId) {
        groups.leave(currentUserId, groupId);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMemberResponse> members(
            @RequestHeader("X-User-Id") UUID currentUserId,
            @PathVariable UUID groupId) {
        return groups.members(currentUserId, groupId);
    }
}
