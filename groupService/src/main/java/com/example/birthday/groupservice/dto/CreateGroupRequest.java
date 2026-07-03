package com.example.birthday.groupservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGroupRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 2000) String description,
        String avatarUrl,
        boolean publicGroup
) {
}
