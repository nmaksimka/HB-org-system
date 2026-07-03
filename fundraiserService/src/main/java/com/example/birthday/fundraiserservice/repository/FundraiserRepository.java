package com.example.birthday.fundraiserservice.repository;

import com.example.birthday.fundraiserservice.model.Fundraiser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FundraiserRepository extends JpaRepository<Fundraiser, UUID> {}
