package com.example.birthday.groupservice.repository;

import com.example.birthday.groupservice.model.BirthdayGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<BirthdayGroup, UUID> {
    Optional<BirthdayGroup> findByIdAndDeletedAtIsNull(UUID id);
    Page<BirthdayGroup> findAllByDeletedAtIsNullAndPublicGroupTrue(Pageable pageable);
}
