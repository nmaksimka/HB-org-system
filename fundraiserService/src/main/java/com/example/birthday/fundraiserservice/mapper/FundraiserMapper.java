package com.example.birthday.fundraiserservice.mapper;

import com.example.birthday.fundraiserservice.dto.*;
import com.example.birthday.fundraiserservice.model.*;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FundraiserMapper {
    FundraiserResponse toResponse(Fundraiser fundraiser);
    ParticipantResponse toResponse(FundraiserParticipant participant);
}
