package com.example.birthday.fundraiserservice.service;

import com.example.birthday.fundraiserservice.client.*;
import com.example.birthday.fundraiserservice.dto.CreateFundraiserRequest;
import com.example.birthday.fundraiserservice.exception.BusinessException;
import com.example.birthday.fundraiserservice.mapper.FundraiserMapper;
import com.example.birthday.fundraiserservice.repository.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraiserServiceTest {
    private final FundraiserRepository fundraisers = mock(FundraiserRepository.class);
    private final FundraiserParticipantRepository participants = mock(FundraiserParticipantRepository.class);
    private final ProcessedEventRepository processed = mock(ProcessedEventRepository.class);
    private final UserClient users = mock(UserClient.class);
    private final GiftClient gifts = mock(GiftClient.class);
    private final MockBankClient bank = mock(MockBankClient.class);
    private final OutboxService outbox = mock(OutboxService.class);
    private final FundraiserService service = new FundraiserService(
            fundraisers, participants, processed, users, gifts, bank,
            Mappers.getMapper(FundraiserMapper.class), outbox);

    @Test
    void cannotCreateForSelf() {
        UUID user = UUID.randomUUID();
        var request = new CreateFundraiserRequest(
                user, UUID.randomUUID(), "Gift", BigDecimal.TEN, "RUB");
        assertThrows(BusinessException.class, () -> service.create(user, request, "correlation"));
        verifyNoInteractions(users, gifts, fundraisers, outbox);
    }
}
