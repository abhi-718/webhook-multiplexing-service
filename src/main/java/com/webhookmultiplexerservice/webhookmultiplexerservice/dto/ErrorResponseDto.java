package com.webhookmultiplexerservice.webhookmultiplexerservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.webhookmultiplexerservice.webhookmultiplexerservice.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private ErrorCode errorType;
    private String message;
    private List<String> missingFields;

    public ErrorResponseDto(ErrorCode errorCode, String message) {
        this.errorType = errorCode;
        this.message = message;
    }
}
