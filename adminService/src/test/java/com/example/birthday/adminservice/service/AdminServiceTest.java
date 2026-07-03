package com.example.birthday.adminservice.service;
import com.example.birthday.adminservice.client.UserClient;import com.example.birthday.adminservice.mapper.AdminMapper;import com.example.birthday.adminservice.repository.*;import org.junit.jupiter.api.Test;import org.mapstruct.factory.Mappers;import java.util.UUID;import static org.mockito.Mockito.*;
class AdminServiceTest{
 @Test void blockCallsUserServiceAndWritesAudit(){var users=mock(UserClient.class);var audit=mock(AuditLogRepository.class);var service=new AdminService(users,audit,mock(AdminImportRepository.class),Mappers.getMapper(AdminMapper.class));UUID admin=UUID.randomUUID(),user=UUID.randomUUID();service.block(admin,user,"c");verify(users).block(user);verify(audit).save(argThat(v->v.getActorId().equals(admin)&&v.getAggregateId().equals(user)));}
}
