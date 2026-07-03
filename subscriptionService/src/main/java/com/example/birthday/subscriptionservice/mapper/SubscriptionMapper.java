package com.example.birthday.subscriptionservice.mapper;

import com.example.birthday.subscriptionservice.dto.SubscriptionResponse;
import com.example.birthday.subscriptionservice.model.BirthdaySubscription;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {
    SubscriptionResponse toResponse(BirthdaySubscription value);
}
