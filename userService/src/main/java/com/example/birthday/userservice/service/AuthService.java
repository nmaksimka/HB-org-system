package com.example.birthday.userservice.service;

import com.example.birthday.userservice.dto.AuthResponse;
import com.example.birthday.userservice.dto.LoginRequest;
import com.example.birthday.userservice.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
