package com.webhookmultiplexerservice.webhookmultiplexerservice.controller;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.RequestDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndPointResponse;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.ConsumerEndpoint;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.WebhookEndpoint;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.WebhookMultiplexerService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webhook-multiplexer-service/v1/")
public class WebhookMultiplexerServiceController {

    @Autowired
    private WebhookMultiplexerService webhookMultiplexerService;

    @PostMapping("/create-webhook-endpoint")
    public ResponseEntity<WebhookEndpoint> createWebhookEndPoint(@RequestBody @Valid WebhookEndPointDto webhookEndPointDto) throws BadRequestException {
        return webhookMultiplexerService.createWebhookEndpoint(webhookEndPointDto);
    }

    @PostMapping("/create-consumer-endpoint")
    public ResponseEntity<ConsumerEndpoint> createConsumerEndpoint(@RequestParam Integer webHookId, @RequestBody @Valid ConsumerEndPointDto consumerEndPointDto) throws BadRequestException {
        return webhookMultiplexerService.createConsumerEndpoint(webHookId, consumerEndPointDto);
    }

    @PostMapping("/forward-request")
    public  List<ConsumerEndPointResponse> forwardRequest(@RequestParam Integer webHookId,
                                                          @RequestBody RequestDto requestDto) throws BadRequestException {
        return webhookMultiplexerService.forwardRequest(webHookId, requestDto);
    }
}
