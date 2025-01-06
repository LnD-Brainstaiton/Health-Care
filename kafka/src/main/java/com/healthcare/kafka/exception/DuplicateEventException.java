package com.healthcare.kafka.exception;

public class DuplicateEventException extends RuntimeException {
    public DuplicateEventException(final String eventId) {
        super("Duplicate event Id: " + eventId.toString());
    }
}
