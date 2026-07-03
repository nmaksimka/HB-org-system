package com.example.birthday.fundraiserservice.repository;

import com.example.birthday.fundraiserservice.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {}
