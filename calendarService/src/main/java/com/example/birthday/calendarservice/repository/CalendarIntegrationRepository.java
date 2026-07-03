package com.example.birthday.calendarservice.repository;
import com.example.birthday.calendarservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CalendarIntegrationRepository extends JpaRepository<CalendarIntegration,UUID>{
    Optional<CalendarIntegration> findByUserIdAndProvider(UUID userId, CalendarProvider provider);
    List<CalendarIntegration> findAllByUserIdOrderByCreatedAt(UUID userId);
}
