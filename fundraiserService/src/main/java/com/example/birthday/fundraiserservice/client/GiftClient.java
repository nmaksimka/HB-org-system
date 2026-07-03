package com.example.birthday.fundraiserservice.client;

import com.example.birthday.contracts.gift.GiftShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "fundraiser-gift-service", url = "${clients.gift-service.url}")
public interface GiftClient {
    @GetMapping("/internal/gifts/{id}")
    GiftShortResponse get(@PathVariable UUID id);
}
