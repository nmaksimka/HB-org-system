package com.example.birthday.notificationservice.repository;

import com.example.birthday.notificationservice.model.DeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, UUID> {
}
