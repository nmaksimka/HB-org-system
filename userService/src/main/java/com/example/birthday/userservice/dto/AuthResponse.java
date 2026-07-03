package com.example.birthday.userservice.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {
}
