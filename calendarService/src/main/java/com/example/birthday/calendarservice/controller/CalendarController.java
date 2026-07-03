package com.example.birthday.calendarservice.controller;
import com.example.birthday.calendarservice.dto.*;
import com.example.birthday.calendarservice.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/v1/calendar") @RequiredArgsConstructor
public class CalendarController{
    private final CalendarService service;
    @PostMapping("/integrations") @ResponseStatus(HttpStatus.CREATED) IntegrationResponse connect(@RequestHeader("X-User-Id")UUID u,@RequestHeader(value="X-Correlation-Id",required=false)String c,@Valid @RequestBody ConnectCalendarRequest r){return service.connect(u,r,c);}
    @GetMapping("/integrations") List<IntegrationResponse> integrations(@RequestHeader("X-User-Id")UUID u){return service.integrations(u);}
    @PostMapping("/events") @ResponseStatus(HttpStatus.CREATED) CalendarEventResponse create(@RequestHeader("X-User-Id")UUID u,@RequestHeader(value="X-Correlation-Id",required=false)String c,@Valid @RequestBody CreateCalendarEventRequest r){return service.create(u,r,c);}
    @GetMapping("/events") List<CalendarEventResponse> events(@RequestHeader("X-User-Id")UUID u){return service.list(u);}
    @PutMapping("/events/{id}") CalendarEventResponse update(@PathVariable UUID id,@RequestHeader("X-User-Id")UUID u,@Valid @RequestBody UpdateCalendarEventRequest r){return service.update(id,u,r);}
    @DeleteMapping("/events/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) void delete(@PathVariable UUID id,@RequestHeader("X-User-Id")UUID u,@RequestHeader(value="X-Correlation-Id",required=false)String c){service.delete(id,u,c);}
}
