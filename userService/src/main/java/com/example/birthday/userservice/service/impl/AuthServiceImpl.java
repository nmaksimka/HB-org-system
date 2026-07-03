package com.example.birthday.userservice.service.impl;

import com.example.birthday.userservice.dto.*;
import com.example.birthday.userservice.exception.BusinessException;
import com.example.birthday.userservice.mapper.UserMapper;
import com.example.birthday.userservice.model.*;
import com.example.birthday.userservice.repository.UserRepository;
import com.example.birthday.userservice.security.JwtService;
import com.example.birthday.userservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper mapper;

    public AuthServiceImpl(
            UserRepository users,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserMapper mapper) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String username = request.username().trim();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Email is already registered");
        }
        if (users.existsByUsernameIgnoreCase(username)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Username is already registered");
        }

        var user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        var profile = new UserProfile();
        profile.setFirstName(request.firstName().trim());
        profile.setLastName(request.lastName());
        profile.setBirthDate(request.birthDate());
        user.setProfile(profile);

        var saved = users.saveAndFlush(user);
        return response(saved);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        var user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(this::invalidCredentials);
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "User account is not active");
        }
        user.setLastLoginAt(Instant.now());
        return response(user);
    }

    private AuthResponse response(User user) {
        return new AuthResponse(
                jwtService.create(user),
                "Bearer",
                jwtService.ttlSeconds(),
                mapper.toResponse(user)
        );
    }

    private BusinessException invalidCredentials() {
        return new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
