package com.example.birthday.mockbankservice.mapper;

import com.example.birthday.mockbankservice.dto.*;
import com.example.birthday.mockbankservice.model.*;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MockBankMapper {
    CollectionResponse toResponse(MockCollection collection);
    PaymentResponse toResponse(MockPayment payment);
}
