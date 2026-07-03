package com.example.birthday.userservice.mapper;

import com.example.birthday.contracts.user.UserShortResponse;
import com.example.birthday.userservice.dto.UserResponse;
import com.example.birthday.userservice.dto.PublicUserResponse;
import com.example.birthday.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        var profile = user.getProfile();
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                user.getStatus().name(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getBirthDate(),
                profile.getAvatarUrl(),
                profile.getBio(),
                profile.isBirthDateVisible(),
                profile.isGiftListVisible()
        );
    }

    public UserShortResponse toShortResponse(User user) {
        var profile = user.getProfile();
        return new UserShortResponse(
                user.getId(),
                user.getUsername(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getBirthDate(),
                user.getStatus().name()
        );
    }

    public PublicUserResponse toPublicResponse(User user) {
        var profile = user.getProfile();
        return new PublicUserResponse(
                user.getId(),
                user.getUsername(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.isBirthDateVisible() ? profile.getBirthDate() : null,
                profile.getAvatarUrl()
        );
    }
}
