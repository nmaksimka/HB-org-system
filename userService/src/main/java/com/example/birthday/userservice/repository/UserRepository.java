package com.example.birthday.userservice.repository;

import com.example.birthday.userservice.model.User;
import com.example.birthday.userservice.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = "profile")
    Optional<User> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "profile")
    Optional<User> findWithProfileById(UUID id);

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = "profile")
    Page<User> findAllByStatusAndUsernameContainingIgnoreCase(
            UserStatus status, String search, Pageable pageable);
}
