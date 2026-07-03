package com.example.birthday.subscriptionservice.client;

import com.example.birthday.contracts.group.GroupShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "group-service", url = "${clients.group-service.url}")
public interface GroupClient {
    @GetMapping("/internal/groups/{id}")
    GroupShortResponse get(@PathVariable UUID id);
}
