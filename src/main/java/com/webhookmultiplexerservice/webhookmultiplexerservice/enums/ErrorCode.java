package com.webhookmultiplexerservice.webhookmultiplexerservice.enums;


public enum ErrorCode {
    INTERNAL_SERVER_ERROR,
    AUTHORIZATION_FAILED,
    ENTITY_NOT_FOUND,
    BAD_REQUEST,
    UNPROCESSABLE_ENTITY,
    ENTITY_ALREADY_EXIST,
    PROCESSING_ERROR;


    @Override
    public String toString() {
        return this.name();
    }
}
