package com.example.birthday.userservice.mapper;

import com.example.birthday.contracts.user.UserShortResponse;
import com.example.birthday.userservice.dto.UserResponse;
import com.example.birthday.userservice.dto.PublicUserResponse;
import com.example.birthday.userservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "birthDate", source = "profile.birthDate")
    @Mapping(target = "avatarUrl", source = "profile.avatarUrl")
    @Mapping(target = "bio", source = "profile.bio")
    @Mapping(target = "birthDateVisible", source = "profile.birthDateVisible")
    @Mapping(target = "giftListVisible", source = "profile.giftListVisible")
    UserResponse toResponse(User user);

    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "birthDate", source = "profile.birthDate")
    UserShortResponse toShortResponse(User user);

    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "birthDate",
            expression = "java(user.getProfile().isBirthDateVisible() ? user.getProfile().getBirthDate() : null)")
    @Mapping(target = "avatarUrl", source = "profile.avatarUrl")
    PublicUserResponse toPublicResponse(User user);
}
