package com.example.birthday.giftservice.repository;

import com.example.birthday.giftservice.model.GiftReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GiftReservationRepository extends JpaRepository<GiftReservation, UUID> {
    boolean existsByGiftId(UUID giftId);
}
