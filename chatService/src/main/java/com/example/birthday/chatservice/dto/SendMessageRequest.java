package com.example.birthday.chatservice.dto;
import jakarta.validation.constraints.*;
public record SendMessageRequest(@NotBlank @Size(max=5000) String content){}
