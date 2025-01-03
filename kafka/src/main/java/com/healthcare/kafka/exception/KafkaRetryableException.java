package com.healthcare.kafka.exception;

public class KafkaRetryableException extends RuntimeException implements Retryable{
    public KafkaRetryableException(Throwable cause) {
        super(cause);
    }
}