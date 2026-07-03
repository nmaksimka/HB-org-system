package com.example.birthday.calendarservice.mapper;
import com.example.birthday.calendarservice.dto.*;
import com.example.birthday.calendarservice.model.*;
import org.mapstruct.*;
@Mapper(componentModel=MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {
    IntegrationResponse toResponse(CalendarIntegration value);
    CalendarEventResponse toResponse(CalendarEvent value);
}
