package com.example.birthday.subscriptionservice.mapper;

import com.example.birthday.subscriptionservice.dto.SubscriptionResponse;
import com.example.birthday.subscriptionservice.model.BirthdaySubscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public SubscriptionResponse toResponse(BirthdaySubscription value) {
        return new SubscriptionResponse(
                value.getId(), value.getSubscriberId(), value.getType().name(),
                value.getTargetUserId(), value.getTargetGroupId(), value.getDaysBefore(),
                value.isActive(), value.getCreatedAt());
    }
}
