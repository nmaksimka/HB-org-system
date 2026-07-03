package com.example.birthday.adminservice.client;
import org.springframework.cloud.openfeign.FeignClient;import org.springframework.web.bind.annotation.*;import java.util.UUID;
@FeignClient(name="admin-user-service",url="${clients.user-service.url}")
public interface UserClient{@PostMapping("/internal/users/{id}/block")void block(@PathVariable UUID id);}
