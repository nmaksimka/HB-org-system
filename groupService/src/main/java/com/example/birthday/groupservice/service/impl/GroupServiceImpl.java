package com.example.birthday.groupservice.service.impl;

import com.example.birthday.groupservice.client.UserClient;
import com.example.birthday.groupservice.dto.*;
import com.example.birthday.groupservice.exception.BusinessException;
import com.example.birthday.groupservice.mapper.GroupMapper;
import com.example.birthday.groupservice.model.*;
import com.example.birthday.groupservice.repository.*;
import com.example.birthday.groupservice.service.GroupService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groups;
    private final GroupMemberRepository members;
    private final UserClient users;
    private final GroupMapper mapper;

    public GroupServiceImpl(
            GroupRepository groups,
            GroupMemberRepository members,
            UserClient users,
            GroupMapper mapper) {
        this.groups = groups;
        this.members = members;
        this.users = users;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public GroupResponse create(UUID currentUserId, CreateGroupRequest request) {
        users.getUser(currentUserId);
        var group = new BirthdayGroup();
        group.setName(request.name().trim());
        group.setDescription(request.description());
        group.setAvatarUrl(request.avatarUrl());
        group.setPublicGroup(request.publicGroup());
        group.setOwnerId(currentUserId);
        group = groups.save(group);

        var owner = new GroupMember();
        owner.setGroup(group);
        owner.setUserId(currentUserId);
        owner.setRole(GroupRole.OWNER);
        members.save(owner);
        return mapper.toResponse(group);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GroupResponse> list(Pageable pageable) {
        return groups.findAllByDeletedAtIsNullAndPublicGroupTrue(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponse get(UUID currentUserId, UUID groupId) {
        var group = requireGroup(groupId);
        requireAccess(currentUserId, group);
        return mapper.toResponse(group);
    }

    @Override
    @Transactional
    public GroupResponse join(UUID currentUserId, UUID groupId) {
        var group = requireGroup(groupId);
        if (!group.isPublicGroup()) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN, "Private group requires an invitation");
        }
        if (members.existsByGroupIdAndUserIdAndStatus(
                groupId, currentUserId, MemberStatus.ACTIVE)) {
            throw new BusinessException(HttpStatus.CONFLICT, "User is already a group member");
        }
        users.getUser(currentUserId);
        var member = new GroupMember();
        member.setGroup(group);
        member.setUserId(currentUserId);
        members.save(member);
        return mapper.toResponse(group);
    }

    @Override
    @Transactional
    public void leave(UUID currentUserId, UUID groupId) {
        var group = requireGroup(groupId);
        if (group.getOwnerId().equals(currentUserId)) {
            throw new BusinessException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Group owner cannot leave the group");
        }
        var member = members.findByGroupIdAndUserIdAndStatus(
                        groupId, currentUserId, MemberStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND, "Active group membership not found"));
        members.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberResponse> members(UUID currentUserId, UUID groupId) {
        var group = requireGroup(groupId);
        requireAccess(currentUserId, group);
        return members.findAllByGroupIdAndStatusOrderByJoinedAt(
                        groupId, MemberStatus.ACTIVE).stream()
                .map(member -> new GroupMemberResponse(
                        users.getUser(member.getUserId()),
                        member.getRole().name(),
                        member.getJoinedAt()))
                .toList();
    }

    private BirthdayGroup requireGroup(UUID groupId) {
        return groups.findByIdAndDeletedAtIsNull(groupId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND, "Group not found"));
    }

    private void requireAccess(UUID userId, BirthdayGroup group) {
        if (!group.isPublicGroup() && !members.existsByGroupIdAndUserIdAndStatus(
                group.getId(), userId, MemberStatus.ACTIVE)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Group access denied");
        }
    }
}
