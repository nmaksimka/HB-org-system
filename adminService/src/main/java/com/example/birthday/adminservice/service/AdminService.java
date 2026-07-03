package com.example.birthday.adminservice.service;
import com.example.birthday.adminservice.client.UserClient;import com.example.birthday.adminservice.dto.*;import com.example.birthday.adminservice.mapper.AdminMapper;import com.example.birthday.adminservice.model.*;import com.example.birthday.adminservice.repository.*;
import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.time.Instant;import java.util.*;
@Service @RequiredArgsConstructor public class AdminService{
 private final UserClient users;private final AuditLogRepository audit;private final AdminImportRepository imports;private final AdminMapper mapper;
 @Transactional public void block(UUID adminId,UUID userId,String correlation){users.block(userId);var l=new AuditLog();l.setEventType("UserBlockedEvent");l.setSourceService("admin-service");l.setActorId(adminId);l.setAggregateId(userId);l.setCorrelationId(correlation);l.setPayload("{\"userId\":\""+userId+"\"}");l.setOccurredAt(Instant.now());audit.save(l);}
 @Transactional(readOnly=true)public Page<AuditLogResponse>audit(Pageable p){return audit.findAllByOrderByOccurredAtDesc(p).map(mapper::toResponse);}
 @Transactional public AdminImportResponse importCsv(UUID admin,String name,String csv){
  var i=new AdminImport();i.setAdminId(admin);i.setFileName(name);String[]lines=csv==null?new String[0]:csv.lines().filter(v->!v.isBlank()).toArray(String[]::new);i.setTotalRows(Math.max(0,lines.length-1));
  int valid=0;for(int x=1;x<lines.length;x++)if(lines[x].split(",",-1).length>=4)valid++;i.setSuccessfulRows(valid);i.setFailedRows(i.getTotalRows()-valid);i.setStatus(i.getFailedRows()==0?ImportStatus.COMPLETED:ImportStatus.FAILED);if(i.getFailedRows()>0)i.setErrorMessage("Some rows do not contain email, username, firstName and birthDate");i.setFinishedAt(Instant.now());return mapper.toResponse(imports.save(i));
 }
 @Transactional public void record(UUID eventId,String type,String source,String correlation,String payload,Instant occurred){
  if(audit.existsByEventId(eventId))return;var l=new AuditLog();l.setEventId(eventId);l.setEventType(type);l.setSourceService(source);l.setCorrelationId(correlation);l.setPayload(payload);l.setOccurredAt(occurred);audit.save(l);
 }
}
