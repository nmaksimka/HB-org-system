package com.example.birthday.mockbankservice.service;

import com.example.birthday.mockbankservice.dto.*;
import com.example.birthday.mockbankservice.exception.BusinessException;
import com.example.birthday.mockbankservice.mapper.MockBankMapper;
import com.example.birthday.mockbankservice.model.*;
import com.example.birthday.mockbankservice.repository.*;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MockBankServiceTest {
    private final MockCollectionRepository collections = mock(MockCollectionRepository.class);
    private final MockPaymentRepository payments = mock(MockPaymentRepository.class);
    private final OutboxService outbox = mock(OutboxService.class);
    private final MockBankMapper mapper = Mappers.getMapper(MockBankMapper.class);
    private final MockBankService service = new MockBankService(collections, payments, mapper, outbox);

    @Test
    void ownerCannotPayOwnCollection() {
        UUID owner = UUID.randomUUID();
        var collection = new MockCollection();
        collection.setId(UUID.randomUUID());
        collection.setFundraiserId(UUID.randomUUID());
        collection.setOwnerId(owner);
        collection.setStatus(CollectionStatus.ACTIVE);
        when(collections.findByFundraiserId(collection.getFundraiserId())).thenReturn(Optional.of(collection));
        when(payments.findByIdempotencyKey("key")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.pay(
                collection.getFundraiserId(), owner,
                new CreatePaymentRequest(BigDecimal.TEN, "key"), "correlation"));
        verifyNoInteractions(outbox);
    }

    @Test
    void idempotencyKeyReturnsExistingPayment() {
        var payment = new MockPayment();
        payment.setId(UUID.randomUUID());
        payment.setStatus(PaymentStatus.SUCCEEDED);
        when(payments.findByIdempotencyKey("same")).thenReturn(Optional.of(payment));

        var result = service.pay(UUID.randomUUID(), UUID.randomUUID(),
                new CreatePaymentRequest(BigDecimal.ONE, "same"), "correlation");

        assertEquals(payment.getId(), result.id());
        verifyNoInteractions(collections, outbox);
    }
}
