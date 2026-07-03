package com.example.birthday.adminservice.dto;
import java.time.Instant;
import java.util.UUID;
public record AdminImportResponse(UUID id,UUID adminId,String fileName,String status,int totalRows,int successfulRows,int failedRows,String errorMessage,Instant createdAt,Instant finishedAt){}
