package com.example.birthday.chatservice.client;
import com.example.birthday.contracts.user.UserShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@FeignClient(name="user-service",url="${clients.user-service.url}")
public interface UserClient{@GetMapping("/internal/users/{id}") UserShortResponse get(@PathVariable UUID id);}
