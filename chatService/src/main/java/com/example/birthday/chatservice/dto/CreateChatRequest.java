package com.example.birthday.chatservice.dto;
import jakarta.validation.constraints.*;
public record CreateChatRequest(@NotBlank @Size(max=255) String title){}
