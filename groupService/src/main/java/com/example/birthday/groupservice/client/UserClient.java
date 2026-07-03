package com.example.birthday.groupservice.client;

import com.example.birthday.contracts.user.UserShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${clients.user-service.url}")
public interface UserClient {
    @GetMapping("/internal/users/{userId}")
    UserShortResponse getUser(@PathVariable UUID userId);
}
