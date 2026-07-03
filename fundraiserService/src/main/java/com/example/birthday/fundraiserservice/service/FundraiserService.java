package com.example.birthday.fundraiserservice.service;

import com.example.birthday.contracts.mockbank.CreateMockCollectionRequest;
import com.example.birthday.fundraiserservice.client.*;
import com.example.birthday.fundraiserservice.dto.*;
import com.example.birthday.fundraiserservice.exception.BusinessException;
import com.example.birthday.fundraiserservice.mapper.FundraiserMapper;
import com.example.birthday.fundraiserservice.model.*;
import com.example.birthday.fundraiserservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FundraiserService {
    private final FundraiserRepository fundraisers;
    private final FundraiserParticipantRepository participants;
    private final ProcessedEventRepository processedEvents;
    private final UserClient users;
    private final GiftClient gifts;
    private final MockBankClient bank;
    private final FundraiserMapper mapper;
    private final OutboxService outbox;

    @Transactional
    public FundraiserResponse create(
            UUID ownerId, CreateFundraiserRequest request, String correlationId) {
        if (ownerId.equals(request.beneficiaryId())) {
            throw new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cannot create fundraiser for yourself");
        }
        users.get(request.beneficiaryId());
        var gift = gifts.get(request.giftId());
        if (!gift.userId().equals(request.beneficiaryId())) {
            throw new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Gift does not belong to beneficiary");
        }
        var fundraiser = new Fundraiser();
        fundraiser.setOwnerId(ownerId);
        fundraiser.setBeneficiaryId(request.beneficiaryId());
        fundraiser.setGiftId(request.giftId());
        fundraiser.setTitle(request.title());
        fundraiser.setTargetAmount(request.targetAmount());
        fundraiser.setCurrency(request.currency());
        fundraiser = fundraisers.save(fundraiser);
        addParticipant(fundraiser.getId(), ownerId);
        outbox.add("fundraiser.created", "FundraiserCreatedEvent", fundraiser.getId(),
                correlationId, eventPayload(fundraiser));
        return mapper.toResponse(fundraiser);
    }

    @Transactional
    public FundraiserResponse activate(UUID id, UUID userId, String correlationId) {
        var fundraiser = owned(id, userId);
        if (fundraiser.getStatus() != FundraiserStatus.DRAFT) {
            throw new BusinessException(HttpStatus.CONFLICT, "Fundraiser is not in draft");
        }
        var collection = bank.create(new CreateMockCollectionRequest(
                fundraiser.getId(), fundraiser.getOwnerId(),
                fundraiser.getTargetAmount(), fundraiser.getCurrency()));
        fundraiser.setPaymentUrl(collection.paymentUrl());
        fundraiser.setStatus(FundraiserStatus.ACTIVE);
        outbox.add("fundraiser.activated", "FundraiserActivatedEvent", id,
                correlationId, eventPayload(fundraiser));
        return mapper.toResponse(fundraiser);
    }

    @Transactional
    public ParticipantResponse join(UUID id, UUID userId) {
        var fundraiser = getEntity(id);
        if (fundraiser.getBeneficiaryId().equals(userId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "Beneficiary cannot participate in own fundraiser");
        }
        if (fundraiser.getStatus() != FundraiserStatus.ACTIVE) {
            throw new BusinessException(HttpStatus.CONFLICT, "Fundraiser is not active");
        }
        users.get(userId);
        return participants.findByFundraiserIdAndUserId(id, userId)
                .map(mapper::toResponse)
                .orElseGet(() -> mapper.toResponse(addParticipant(id, userId)));
    }

    @Transactional(readOnly = true)
    public FundraiserResponse get(UUID id, UUID userId) {
        var fundraiser = getEntity(id);
        if (fundraiser.getBeneficiaryId().equals(userId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN,
                    "Beneficiary cannot view own fundraiser");
        }
        return mapper.toResponse(fundraiser);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> participants(UUID id, UUID userId) {
        get(id, userId);
        return participants.findAllByFundraiserIdOrderByJoinedAt(id).stream()
                .map(mapper::toResponse).toList();
    }

    @Transactional
    public void paymentSucceeded(
            UUID eventId, UUID paymentId, UUID fundraiserId,
            UUID payerId, BigDecimal amount, String correlationId) {
        if (processedEvents.existsById(eventId)) return;
        var fundraiser = getEntity(fundraiserId);
        if (fundraiser.getStatus() != FundraiserStatus.ACTIVE) {
            processedEvents.save(new ProcessedEvent(eventId, "PaymentSucceededEvent"));
            return;
        }
        var participant = participants.findByFundraiserIdAndUserId(fundraiserId, payerId)
                .orElseGet(() -> addParticipant(fundraiserId, payerId));
        participant.setContributedAmount(participant.getContributedAmount().add(amount));
        fundraiser.setCollectedAmount(fundraiser.getCollectedAmount().add(amount));
        if (fundraiser.getCollectedAmount().compareTo(fundraiser.getTargetAmount()) >= 0) {
            fundraiser.setStatus(FundraiserStatus.COMPLETED);
            outbox.add("fundraiser.completed", "FundraiserCompletedEvent", fundraiserId,
                    correlationId, Map.of("fundraiserId", fundraiserId,
                            "collectedAmount", fundraiser.getCollectedAmount()));
        }
        processedEvents.save(new ProcessedEvent(eventId, "PaymentSucceededEvent"));
    }

    private FundraiserParticipant addParticipant(UUID fundraiserId, UUID userId) {
        var participant = new FundraiserParticipant();
        participant.setFundraiserId(fundraiserId);
        participant.setUserId(userId);
        participant.setContributedAmount(BigDecimal.ZERO);
        return participants.save(participant);
    }
    private Fundraiser owned(UUID id, UUID userId) {
        var fundraiser = getEntity(id);
        if (!fundraiser.getOwnerId().equals(userId)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Only owner can change fundraiser");
        }
        return fundraiser;
    }
    private Fundraiser getEntity(UUID id) {
        return fundraisers.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Fundraiser not found"));
    }
    private Map<String, Object> eventPayload(Fundraiser fundraiser) {
        return Map.of("fundraiserId", fundraiser.getId(), "ownerId", fundraiser.getOwnerId(),
                "beneficiaryId", fundraiser.getBeneficiaryId(), "giftId", fundraiser.getGiftId(),
                "targetAmount", fundraiser.getTargetAmount(), "currency", fundraiser.getCurrency());
    }
}
