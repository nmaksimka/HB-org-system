package com.example.birthday.fundraiserservice.client;

import com.example.birthday.contracts.mockbank.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "fundraiser-mock-bank-service", url = "${clients.mock-bank-service.url}")
public interface MockBankClient {
    @PostMapping("/internal/mock-bank/collections")
    MockCollectionResponse create(@RequestBody CreateMockCollectionRequest request);
}
