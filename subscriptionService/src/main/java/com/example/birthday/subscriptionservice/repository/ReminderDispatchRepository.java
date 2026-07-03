package com.example.birthday.subscriptionservice.repository;

import com.example.birthday.subscriptionservice.model.ReminderDispatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReminderDispatchRepository extends JpaRepository<ReminderDispatch, UUID> {
    boolean existsBySubscriptionIdAndBirthdayYear(UUID subscriptionId, int birthdayYear);
}
