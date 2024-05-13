package com.webhookmultiplexerservice.webhookmultiplexerservice.service;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointResponseDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndPointResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerEndPointResponseMapper {

    ConsumerEndPointResponseDto toEntity(ConsumerEndPointResponse consumerEndPointResponse);
}
