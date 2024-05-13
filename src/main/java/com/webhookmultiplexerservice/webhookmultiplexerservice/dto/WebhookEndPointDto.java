package com.webhookmultiplexerservice.webhookmultiplexerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEndPointDto {
    @NotEmpty
    private String apiName;
    @NotEmpty
    private String endPoint;
}
