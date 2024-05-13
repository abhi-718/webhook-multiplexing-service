package com.webhookmultiplexerservice.webhookmultiplexerservice.service;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndpoint;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.WebhookEndpoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ConsumerEndPointMapper {

    @Mapping(target = "name", source = "apiName")
    @Mapping(target = "endPoint", source = "endPoint")
    @Mapping(target = "webhookId", source = "consumerEndPointDto.webHookEndPoint.id")
    ConsumerEndpoint toEntity(ConsumerEndPointDto consumerEndPointDto);
}
