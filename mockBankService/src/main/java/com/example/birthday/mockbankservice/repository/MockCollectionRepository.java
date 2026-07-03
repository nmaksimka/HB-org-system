package com.example.birthday.mockbankservice.repository;

import com.example.birthday.mockbankservice.model.MockCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MockCollectionRepository extends JpaRepository<MockCollection, UUID> {
    Optional<MockCollection> findByFundraiserId(UUID fundraiserId);
}
