package com.webhookmultiplexerservice.webhookmultiplexerservice.dto;

import com.webhookmultiplexerservice.webhookmultiplexerservice.model.WebhookEndpoint;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEndPointDto {
    @NotEmpty
    private String apiName;
    @NotEmpty
    private String EndPoint;

    private WebhookEndpoint webHookEndPoint;
}

