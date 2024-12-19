package com.health_care.unique_id_generator.Api;

public interface UniqueIdGenerator {
    String generateUniqueIdForRequestId();
    String generateUniqueIdWithPrefix(String prefix);
}