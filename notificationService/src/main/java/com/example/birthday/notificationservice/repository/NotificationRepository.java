package com.example.birthday.notificationservice.repository;

import com.example.birthday.notificationservice.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    boolean existsBySourceEventId(UUID sourceEventId);
    Page<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    Page<Notification> findAllByUserIdAndStatusOrderByCreatedAtDesc(
            UUID userId, NotificationStatus status, Pageable pageable);
    Optional<Notification> findByIdAndUserId(UUID id, UUID userId);

    List<Notification> findAllByUserIdAndStatus(UUID userId, NotificationStatus status);
}
