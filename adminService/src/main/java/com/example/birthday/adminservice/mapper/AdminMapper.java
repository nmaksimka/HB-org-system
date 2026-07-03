package com.example.birthday.adminservice.mapper;
import com.example.birthday.adminservice.dto.*;import com.example.birthday.adminservice.model.*;import org.mapstruct.*;
@Mapper(componentModel=MappingConstants.ComponentModel.SPRING)
public interface AdminMapper{AuditLogResponse toResponse(AuditLog value);AdminImportResponse toResponse(AdminImport value);}
