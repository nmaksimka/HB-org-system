package com.example.birthday.groupservice.controller;

import com.example.birthday.contracts.group.GroupShortResponse;
import com.example.birthday.groupservice.exception.BusinessException;
import com.example.birthday.groupservice.repository.GroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/groups")
public class InternalGroupController {
    private final GroupRepository groups;

    public InternalGroupController(GroupRepository groups) {
        this.groups = groups;
    }

    @GetMapping("/{groupId}")
    public GroupShortResponse get(@PathVariable UUID groupId) {
        var group = groups.findByIdAndDeletedAtIsNull(groupId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Group not found"));
        return new GroupShortResponse(
                group.getId(), group.getName(), group.getOwnerId(), group.isPublicGroup());
    }
}
