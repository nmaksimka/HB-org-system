package com.example.birthday.groupservice.mapper;

import com.example.birthday.groupservice.dto.GroupResponse;
import com.example.birthday.groupservice.model.BirthdayGroup;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {
    GroupResponse toResponse(BirthdayGroup group);
}
