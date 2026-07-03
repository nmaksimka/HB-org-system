package com.example.birthday.calendarservice.repository;
import com.example.birthday.calendarservice.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OutboxEventRepository extends JpaRepository<OutboxEvent,UUID>{
    List<OutboxEvent> findByStatusOrderByCreatedAt(OutboxStatus status, Pageable pageable);
}
