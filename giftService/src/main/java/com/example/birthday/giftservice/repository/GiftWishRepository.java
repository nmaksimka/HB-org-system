package com.example.birthday.giftservice.repository;

import com.example.birthday.giftservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;

import java.util.*;

public interface GiftWishRepository extends JpaRepository<GiftWish, UUID> {
    Optional<GiftWish> findByIdAndDeletedAtIsNull(UUID id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from GiftWish g where g.id = :id and g.deletedAt is null")
    Optional<GiftWish> findForUpdate(UUID id);
    List<GiftWish> findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID userId);
    List<GiftWish> findAllByUserIdAndVisibilityAndDeletedAtIsNullOrderByCreatedAtDesc(
            UUID userId, GiftVisibility visibility);
}
