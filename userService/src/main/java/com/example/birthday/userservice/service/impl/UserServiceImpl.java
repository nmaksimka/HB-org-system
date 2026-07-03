package com.example.birthday.userservice.service.impl;

import com.example.birthday.contracts.user.UserShortResponse;
import com.example.birthday.userservice.dto.UpdateProfileRequest;
import com.example.birthday.userservice.dto.UserResponse;
import com.example.birthday.userservice.dto.PublicUserResponse;
import com.example.birthday.userservice.exception.BusinessException;
import com.example.birthday.userservice.mapper.UserMapper;
import com.example.birthday.userservice.model.User;
import com.example.birthday.userservice.model.UserStatus;
import com.example.birthday.userservice.repository.UserRepository;
import com.example.birthday.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository users, UserMapper mapper) {
        this.users = users;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getOwnProfile(UUID id) {
        return mapper.toResponse(requireActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PublicUserResponse getPublicProfile(UUID id) {
        return mapper.toPublicResponse(requireActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserShortResponse getInternal(UUID id) {
        return mapper.toShortResponse(requireActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicUserResponse> list(String search, Pageable pageable) {
        return users.findAllByStatusAndUsernameContainingIgnoreCase(
                UserStatus.ACTIVE, search == null ? "" : search.trim(), pageable
        ).map(mapper::toPublicResponse);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UUID currentUserId, UpdateProfileRequest request) {
        var user = requireActive(currentUserId);
        var profile = user.getProfile();
        profile.setFirstName(request.firstName().trim());
        profile.setLastName(request.lastName());
        if (request.birthDate() != null) {
            profile.setBirthDate(request.birthDate());
        }
        profile.setAvatarUrl(request.avatarUrl());
        profile.setBio(request.bio());
        profile.setBirthDateVisible(request.birthDateVisible());
        profile.setGiftListVisible(request.giftListVisible());
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void block(UUID id) {
        User user = users.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User not found"));
        user.setStatus(UserStatus.BLOCKED);
    }

    private User requireActive(UUID id) {
        User user = users.findWithProfileById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }
}
