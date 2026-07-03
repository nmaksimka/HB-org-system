package com.example.birthday.mockbankservice.service;

import com.example.birthday.mockbankservice.dto.*;
import com.example.birthday.mockbankservice.exception.BusinessException;
import com.example.birthday.mockbankservice.mapper.MockBankMapper;
import com.example.birthday.mockbankservice.model.*;
import com.example.birthday.mockbankservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MockBankService {
    private final MockCollectionRepository collections;
    private final MockPaymentRepository payments;
    private final MockBankMapper mapper;
    private final OutboxService outbox;

    @Transactional
    public CollectionResponse createCollection(CreateCollectionRequest request) {
        return collections.findByFundraiserId(request.fundraiserId()).map(mapper::toResponse)
                .orElseGet(() -> mapper.toResponse(saveCollection(request)));
    }

    private MockCollection saveCollection(CreateCollectionRequest request) {
        var collection = new MockCollection();
        collection.setFundraiserId(request.fundraiserId());
        collection.setOwnerId(request.ownerId());
        collection.setTargetAmount(request.targetAmount());
        collection.setCurrency(request.currency());
        collection.setStatus(CollectionStatus.ACTIVE);
        collection.setPaymentUrl("/api/v1/mock-bank/collections/" + request.fundraiserId() + "/payments");
        return collections.save(collection);
    }

    @Transactional(readOnly = true)
    public CollectionResponse getByFundraiser(UUID fundraiserId) {
        return mapper.toResponse(collections.findByFundraiserId(fundraiserId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Collection not found")));
    }

    @Transactional
    public PaymentResponse pay(
            UUID fundraiserId, UUID payerId, CreatePaymentRequest request, String correlationId) {
        var existing = payments.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) return mapper.toResponse(existing.get());

        var collection = collections.findByFundraiserId(fundraiserId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Collection not found"));
        if (collection.getStatus() != CollectionStatus.ACTIVE) {
            throw new BusinessException(HttpStatus.CONFLICT, "Collection is not active");
        }
        if (collection.getOwnerId().equals(payerId)) {
            throw new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY, "Owner cannot pay own collection");
        }

        var payment = new MockPayment();
        payment.setCollectionId(collection.getId());
        payment.setPayerId(payerId);
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setIdempotencyKey(request.idempotencyKey());
        payment = payments.save(payment);

        outbox.add("payment.succeeded", "PaymentSucceededEvent", payment.getId(),
                correlationId == null ? UUID.randomUUID().toString() : correlationId,
                Map.of("paymentId", payment.getId(), "fundraiserId", collection.getFundraiserId(),
                        "payerId", payerId, "amount", payment.getAmount(),
                        "currency", collection.getCurrency()));
        return mapper.toResponse(payment);
    }

    @Transactional
    public void close(UUID fundraiserId) {
        var collection = collections.findByFundraiserId(fundraiserId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Collection not found"));
        collection.setStatus(CollectionStatus.CLOSED);
    }
}
