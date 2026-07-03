package com.example.birthday.mockbankservice.controller;

import com.example.birthday.mockbankservice.dto.*;
import com.example.birthday.mockbankservice.service.MockBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/internal/mock-bank/collections")
@RequiredArgsConstructor
public class InternalMockBankController {
    private final MockBankService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CollectionResponse create(@Valid @RequestBody CreateCollectionRequest request) {
        return service.createCollection(request);
    }

    @PostMapping("/{fundraiserId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void close(@PathVariable UUID fundraiserId) {
        service.close(fundraiserId);
    }
}
