package com.webhookmultiplexerservice.webhookmultiplexerservice.service;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.WebhookEndpoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebhookEndpointMapper {


    @Mapping(target = "name", source = "apiName")
    @Mapping(target = "endPoint", source = "endPoint")
    WebhookEndpoint toEntity(WebhookEndPointDto webhookEndPointDto);
}
