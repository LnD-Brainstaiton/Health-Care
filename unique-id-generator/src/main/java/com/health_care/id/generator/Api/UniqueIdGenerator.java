package com.health_care.id.generator.Api;

public interface UniqueIdGenerator {
    String generateUniqueIdForRequestId();
    String generateUniqueIdWithPrefix(String prefix);
}