package com.health_care.gateway.exceptions;

public class MissingAuthorizationHeaderException extends RuntimeException {
    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}