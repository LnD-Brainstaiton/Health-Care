package com.healthcare.userservice.service.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.userservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.userservice.domain.dto.IdTokenDto;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IdTokenRedisService extends BaseService {

    public final ObjectMapper objectMapper;

    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${id-token.redis.expiry-in-seconds}")
    protected String idTokenTtlInSeconds;

    public void saveToRedis(final String key, final String idToken) {
        final long idTokenTtlInSec = Long.parseLong(idTokenTtlInSeconds);
        try {
            redisTemplate.opsForValue().set(key, idToken, idTokenTtlInSec, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new InvalidRequestDataException(ResponseMessage.REDIS_PUSH_EXCEPTION);
        }
    }

    public IdTokenDto getFromRedis(final String key) {
        try {
            return objectMapper.readValue(
                    redisTemplate.opsForValue().get(key), IdTokenDto.class
            );
        } catch (Exception ex) {
            throw new InvalidRequestDataException(ResponseMessage.ID_TOKEN_FETCH_ERROR);
        }
    }

    public void deleteAllByPrefix(final String prefix) {
        final ScanOptions scanOptions = ScanOptions.scanOptions().match(prefix + ":*").count(5000).build();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            Cursor<byte[]> cursor = connection.scan(scanOptions);

            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                connection.del(key);
            }

            cursor.close();
            return null;
        });
    }
}
