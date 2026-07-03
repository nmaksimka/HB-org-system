package com.example.birthday.userservice.service;

import com.example.birthday.contracts.user.UserShortResponse;
import com.example.birthday.userservice.dto.UpdateProfileRequest;
import com.example.birthday.userservice.dto.UserResponse;
import com.example.birthday.userservice.dto.PublicUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse getOwnProfile(UUID id);
    PublicUserResponse getPublicProfile(UUID id);
    UserShortResponse getInternal(UUID id);
    Page<PublicUserResponse> list(String search, Pageable pageable);
    UserResponse updateProfile(UUID currentUserId, UpdateProfileRequest request);
}
