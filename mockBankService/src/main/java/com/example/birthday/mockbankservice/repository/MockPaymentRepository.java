package com.example.birthday.mockbankservice.repository;

import com.example.birthday.mockbankservice.model.MockPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MockPaymentRepository extends JpaRepository<MockPayment, UUID> {
    Optional<MockPayment> findByIdempotencyKey(String idempotencyKey);
}
