package com.healthcare.userservice.service.redis;

import com.healthcare.userservice.domain.dto.IdTokenDto;
import com.healthcare.userservice.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService extends BaseService {

    private final IdTokenRedisService idTokenRedisService;

    public void pushTokenToRedis(final String sessionId, final String idTokenDto) {
        idTokenRedisService.saveToRedis(sessionId, idTokenDto);
    }

    public IdTokenDto getIdTokenFromRedis(final String sessionId) {
        return idTokenRedisService.getFromRedis(sessionId);
    }

    public void deleteAllByPrefix(final String prefixKey) {
        idTokenRedisService.deleteAllByPrefix(prefixKey);
    }

}