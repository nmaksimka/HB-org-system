package com.example.birthday.fundraiserservice.controller;

import com.example.birthday.fundraiserservice.dto.*;
import com.example.birthday.fundraiserservice.service.FundraiserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/fundraisers")
@RequiredArgsConstructor
public class FundraiserController {
    private final FundraiserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FundraiserResponse create(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @Valid @RequestBody CreateFundraiserRequest request) {
        return service.create(userId, request, correlationId);
    }
    @PostMapping("/{id}/activate")
    FundraiserResponse activate(
            @PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {
        return service.activate(id, userId, correlationId);
    }
    @PostMapping("/{id}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipantResponse join(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return service.join(id, userId);
    }
    @GetMapping("/{id}")
    FundraiserResponse get(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return service.get(id, userId);
    }
    @GetMapping("/{id}/participants")
    List<ParticipantResponse> participants(
            @PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return service.participants(id, userId);
    }
}
