package com.webhookmultiplexerservice.webhookmultiplexerservice.service;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.RequestDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndPointResponse;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndpoint;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.WebhookEndpoint;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WebhookMultiplexerService {
    ResponseEntity<WebhookEndpoint> createWebhookEndpoint(WebhookEndPointDto webhookEndPointDto) throws BadRequestException;

    ResponseEntity<ConsumerEndpoint> createConsumerEndpoint(Integer webHookId, ConsumerEndPointDto consumerEndPointDto) throws BadRequestException;

    List<ConsumerEndPointResponse> forwardRequest(Integer webHookId, RequestDto requestDto) throws BadRequestException;
}
