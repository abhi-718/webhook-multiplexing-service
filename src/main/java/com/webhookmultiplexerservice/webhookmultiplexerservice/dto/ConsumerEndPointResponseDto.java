package com.webhookmultiplexerservice.webhookmultiplexerservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerEndPointResponseDto {

    private String webHookEndPoint;
    private String consumerEndPoint;
    private String requestBody;
    private String headers;
    private String requestMethod;
    private String requestParam;
    private String consumerApiResponse;
    private String consumerApiResponseStatusCode;
    private String consumerApiResponseStatus;
}
