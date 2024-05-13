package com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEndPointResponseDto {

    private  String statusCode;

    private String response;
}
