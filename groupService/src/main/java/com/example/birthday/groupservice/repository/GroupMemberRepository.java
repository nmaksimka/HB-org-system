package com.example.birthday.groupservice.repository;

import com.example.birthday.groupservice.model.GroupMember;
import com.example.birthday.groupservice.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    boolean existsByGroupIdAndUserIdAndStatus(UUID groupId, UUID userId, MemberStatus status);
    Optional<GroupMember> findByGroupIdAndUserIdAndStatus(UUID groupId, UUID userId, MemberStatus status);
    List<GroupMember> findAllByGroupIdAndStatusOrderByJoinedAt(
            UUID groupId, MemberStatus status);
}
