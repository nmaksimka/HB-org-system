package com.example.birthday.userservice.service;

import com.example.birthday.userservice.dto.RegisterRequest;
import com.example.birthday.userservice.exception.BusinessException;
import com.example.birthday.userservice.mapper.UserMapper;
import com.example.birthday.userservice.repository.UserRepository;
import com.example.birthday.userservice.security.JwtService;
import com.example.birthday.userservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock UserRepository users;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;

    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(
                users, passwordEncoder, jwtService, Mappers.getMapper(UserMapper.class));
    }

    @Test
    void rejectsDuplicateEmail() {
        var request = new RegisterRequest(
                "max@example.com",
                "maksim",
                "password123",
                "Максим",
                "Никонов",
                LocalDate.of(2008, 4, 15)
        );
        when(users.existsByEmailIgnoreCase("max@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email is already registered");
    }
}
