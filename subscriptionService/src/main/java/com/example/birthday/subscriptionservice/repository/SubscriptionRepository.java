package com.example.birthday.subscriptionservice.repository;

import com.example.birthday.subscriptionservice.model.BirthdaySubscription;
import com.example.birthday.subscriptionservice.model.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface SubscriptionRepository extends JpaRepository<BirthdaySubscription, UUID> {
    boolean existsBySubscriberIdAndTargetUserIdAndActiveTrue(UUID subscriberId, UUID targetUserId);
    boolean existsBySubscriberIdAndTargetGroupIdAndActiveTrue(UUID subscriberId, UUID targetGroupId);
    List<BirthdaySubscription> findAllBySubscriberIdAndActiveTrueOrderByCreatedAtDesc(UUID subscriberId);
    Optional<BirthdaySubscription> findByIdAndSubscriberIdAndActiveTrue(UUID id, UUID subscriberId);
    List<BirthdaySubscription> findAllByTypeAndActiveTrue(SubscriptionType type);
}
