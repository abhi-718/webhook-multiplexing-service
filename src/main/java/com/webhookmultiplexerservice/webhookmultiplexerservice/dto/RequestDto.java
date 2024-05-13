package com.webhookmultiplexerservice.webhookmultiplexerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data


public class RequestDto {


    private String requestBody;

    private String headers;

    private String requestMethod;

    private String requestParameter;
}
