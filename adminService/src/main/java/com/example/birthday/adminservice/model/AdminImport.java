package com.example.birthday.adminservice.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="admin_imports") @Getter @Setter @NoArgsConstructor
public class AdminImport{
 @Id private UUID id;@Column(name="admin_id",nullable=false)private UUID adminId;@Column(name="file_name",nullable=false)private String fileName;
 @Enumerated(EnumType.STRING)@Column(nullable=false,length=20)private ImportStatus status;@Column(name="total_rows",nullable=false)private int totalRows;
 @Column(name="successful_rows",nullable=false)private int successfulRows;@Column(name="failed_rows",nullable=false)private int failedRows;
 @Column(name="error_message",columnDefinition="text")private String errorMessage;@Column(name="created_at",nullable=false,updatable=false)private Instant createdAt;@Column(name="finished_at")private Instant finishedAt;
 @PrePersist void create(){if(id==null)id=UUID.randomUUID();createdAt=Instant.now();}
}
