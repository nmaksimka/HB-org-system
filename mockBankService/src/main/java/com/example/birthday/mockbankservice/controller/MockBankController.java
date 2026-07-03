package com.example.birthday.mockbankservice.controller;

import com.example.birthday.mockbankservice.dto.*;
import com.example.birthday.mockbankservice.service.MockBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mock-bank")
@RequiredArgsConstructor
public class MockBankController {
    private final MockBankService service;

    @GetMapping("/collections/{fundraiserId}")
    CollectionResponse get(@PathVariable UUID fundraiserId) {
        return service.getByFundraiser(fundraiserId);
    }

    @PostMapping("/collections/{fundraiserId}/payments")
    @ResponseStatus(HttpStatus.CREATED)
    PaymentResponse pay(
            @PathVariable UUID fundraiserId,
            @RequestHeader("X-User-Id") UUID payerId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @Valid @RequestBody CreatePaymentRequest request) {
        return service.pay(fundraiserId, payerId, request, correlationId);
    }
}
