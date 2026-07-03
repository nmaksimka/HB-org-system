package com.example.birthday.calendarservice.service;
import com.example.birthday.calendarservice.dto.*;
import com.example.birthday.calendarservice.exception.BusinessException;
import com.example.birthday.calendarservice.mapper.CalendarMapper;
import com.example.birthday.calendarservice.model.*;
import com.example.birthday.calendarservice.repository.*;
import com.example.birthday.calendarservice.security.TokenCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service @RequiredArgsConstructor
public class CalendarService{
    private final CalendarIntegrationRepository integrations;private final CalendarEventRepository events;
    private final CalendarMapper mapper;private final TokenCipher cipher;private final OutboxService outbox;
    @Transactional public IntegrationResponse connect(UUID user,ConnectCalendarRequest r,String correlation){
        var value=integrations.findByUserIdAndProvider(user,r.provider()).orElseGet(CalendarIntegration::new);
        value.setUserId(user);value.setProvider(r.provider());value.setCalendarId(r.calendarId());value.setEncryptedAccessToken(cipher.encrypt(r.accessToken()));value.setStatus(IntegrationStatus.ACTIVE);
        value=integrations.save(value);outbox.add("calendar.connected","CalendarConnectedEvent",value.getId(),correlation,Map.of("integrationId",value.getId(),"userId",user,"provider",value.getProvider()));return mapper.toResponse(value);
    }
    @Transactional(readOnly=true) public List<IntegrationResponse> integrations(UUID user){return integrations.findAllByUserIdOrderByCreatedAt(user).stream().map(mapper::toResponse).toList();}
    @Transactional public CalendarEventResponse create(UUID user,CreateCalendarEventRequest r,String correlation){
        var integration=integration(r.integrationId(),user);if(integration.getStatus()!=IntegrationStatus.ACTIVE)throw new BusinessException(HttpStatus.CONFLICT,"Calendar is disconnected");
        cipher.decrypt(integration.getEncryptedAccessToken());
        var event=new CalendarEvent();event.setIntegrationId(integration.getId());event.setUserId(user);event.setBirthdayUserId(r.birthdayUserId());event.setTitle(r.title());event.setEventDate(r.eventDate());event.setExternalEventId(integration.getProvider().name().toLowerCase()+"-"+UUID.randomUUID());event=events.save(event);
        outbox.add("calendar.event.created","CalendarEventCreatedEvent",event.getId(),correlation,Map.of("calendarEventId",event.getId(),"userId",user,"birthdayUserId",r.birthdayUserId(),"eventDate",r.eventDate().toString()));return mapper.toResponse(event);
    }
    @Transactional public CalendarEventResponse update(UUID id,UUID user,UpdateCalendarEventRequest r){
        var e=event(id,user);if(e.getStatus()==CalendarEventStatus.DELETED)throw new BusinessException(HttpStatus.CONFLICT,"Calendar event is deleted");e.setTitle(r.title());e.setEventDate(r.eventDate());return mapper.toResponse(e);
    }
    @Transactional public void delete(UUID id,UUID user,String correlation){var e=event(id,user);e.setStatus(CalendarEventStatus.DELETED);outbox.add("calendar.event.deleted","CalendarEventDeletedEvent",id,correlation,Map.of("calendarEventId",id,"userId",user));}
    @Transactional(readOnly=true) public List<CalendarEventResponse> list(UUID user){return events.findAllByUserIdOrderByEventDate(user).stream().map(mapper::toResponse).toList();}
    private CalendarIntegration integration(UUID id,UUID user){var v=integrations.findById(id).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"Calendar integration not found"));if(!v.getUserId().equals(user))throw new BusinessException(HttpStatus.FORBIDDEN,"Calendar integration does not belong to user");return v;}
    private CalendarEvent event(UUID id,UUID user){return events.findByIdAndUserId(id,user).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"Calendar event not found"));}
}
