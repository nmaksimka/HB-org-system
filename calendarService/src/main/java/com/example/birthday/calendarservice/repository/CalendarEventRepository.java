package com.example.birthday.calendarservice.repository;
import com.example.birthday.calendarservice.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CalendarEventRepository extends JpaRepository<CalendarEvent,UUID>{
    Optional<CalendarEvent> findByIdAndUserId(UUID id,UUID userId);
    List<CalendarEvent> findAllByUserIdOrderByEventDate(UUID userId);
}
