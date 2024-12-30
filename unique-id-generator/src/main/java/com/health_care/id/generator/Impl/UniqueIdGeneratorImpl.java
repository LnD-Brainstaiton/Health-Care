package com.health_care.id.generator.Impl;

import com.health_care.id.generator.Api.UniqueIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Primary
@Service("uniqueIdGenerator")
public class UniqueIdGeneratorImpl implements UniqueIdGenerator {
    @Override
    public String generateUniqueIdForRequestId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    @Override
    public String generateUniqueIdWithPrefix(String prefix) {
        int uuidLength = 8 - prefix.length();

        String uniqueId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, uuidLength);
        return prefix + uniqueId;
    }


}
