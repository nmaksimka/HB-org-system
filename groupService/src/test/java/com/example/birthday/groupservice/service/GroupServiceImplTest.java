package com.example.birthday.groupservice.service;

import com.example.birthday.groupservice.client.UserClient;
import com.example.birthday.groupservice.dto.CreateGroupRequest;
import com.example.birthday.groupservice.exception.BusinessException;
import com.example.birthday.groupservice.mapper.GroupMapper;
import com.example.birthday.groupservice.model.*;
import com.example.birthday.groupservice.repository.*;
import com.example.birthday.groupservice.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    @Mock GroupRepository groups;
    @Mock GroupMemberRepository members;
    @Mock UserClient users;
    private GroupServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GroupServiceImpl(
                groups, members, users, Mappers.getMapper(GroupMapper.class));
    }

    @Test
    void creatorBecomesOwner() {
        UUID userId = UUID.randomUUID();
        when(groups.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        service.create(userId, new CreateGroupRequest("Team", null, null, true));

        var captor = ArgumentCaptor.forClass(GroupMember.class);
        verify(members).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
        assertThat(captor.getValue().getRole()).isEqualTo(GroupRole.OWNER);
    }

    @Test
    void ownerCannotLeave() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        var group = new BirthdayGroup();
        group.setOwnerId(userId);
        when(groups.findByIdAndDeletedAtIsNull(groupId)).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> service.leave(userId, groupId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Group owner cannot leave the group");
    }
}
