package com.example.birthday.adminservice.repository;
import com.example.birthday.adminservice.model.AdminImport;import org.springframework.data.jpa.repository.JpaRepository;import java.util.UUID;
public interface AdminImportRepository extends JpaRepository<AdminImport,UUID>{}
