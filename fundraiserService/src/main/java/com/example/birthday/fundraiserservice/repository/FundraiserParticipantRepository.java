package com.example.birthday.fundraiserservice.repository;

import com.example.birthday.fundraiserservice.model.FundraiserParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface FundraiserParticipantRepository extends JpaRepository<FundraiserParticipant, UUID> {
    Optional<FundraiserParticipant> findByFundraiserIdAndUserId(UUID fundraiserId, UUID userId);
    List<FundraiserParticipant> findAllByFundraiserIdOrderByJoinedAt(UUID fundraiserId);
}
