package com.example.birthday.adminservice.repository;
import com.example.birthday.adminservice.model.AuditLog;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.JpaRepository;import java.util.UUID;
public interface AuditLogRepository extends JpaRepository<AuditLog,UUID>{boolean existsByEventId(UUID eventId);Page<AuditLog>findAllByOrderByOccurredAtDesc(Pageable pageable);}
