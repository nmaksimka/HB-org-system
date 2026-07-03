package com.example.birthday.contracts.group;

import java.util.UUID;

public record GroupShortResponse(
        UUID id,
        String name,
        UUID ownerId,
        boolean publicGroup
) {
}
