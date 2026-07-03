package com.example.birthday.groupservice.dto;

import com.example.birthday.contracts.user.UserShortResponse;

import java.time.Instant;

public record GroupMemberResponse(
        UserShortResponse user,
        String role,
        Instant joinedAt
) {
}
