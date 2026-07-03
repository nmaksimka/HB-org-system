package com.example.birthday.userservice.security;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String email, String role) {
}
