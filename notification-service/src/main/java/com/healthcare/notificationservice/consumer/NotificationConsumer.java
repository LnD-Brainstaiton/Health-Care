package com.healthcare.notificationservice.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.kafka.exception.DuplicateEventException;
import com.healthcare.kafka.exception.Retryable;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;
import com.healthcare.notificationservice.service.impl.kafka.ProcessedEventRepositoryService;
import com.healthcare.notificationservice.service.interfaces.consumer.IIdempotentConsumerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final IIdempotentConsumerService iIdempotentConsumerService;

    private final ProcessedEventRepositoryService repositoryService;

    protected ObjectMapper objectMapper;

    @KafkaListener(topics = "${healthcare.kafka.topic.dynamic-notification}", groupId = "${healthcare.kafka.consumer.group-id}")
    public void intraFundTransferProcessListener(@Payload List<String> events,
                                                 @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                                                 @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                                                 @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        System.out.println(String.format(
                "%s number of payment responses received with keys: %s, partitions: %s and offsets: %s",
                events.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()));


        IntStream.range(0, events.size())
                .forEach(i -> {
                    try {
                        EventWrapper<NotificationEvent> eventWrapper = objectMapper.readValue(events.get(i), new TypeReference<>() {
                        });

                        try {
                            iIdempotentConsumerService.processIdempotentAndOutboxForNotification(eventWrapper);

                            repositoryService.updateProcessedEventStatus(eventWrapper.getEventId());
                        } catch (DuplicateEventException e) {
                        } catch (Exception e) {
                            if (e instanceof Retryable) {
                                repositoryService.saveRetriedProcessedEventStatus(eventWrapper.getEventId());
                                throw e;
                            }
                            repositoryService.saveFailedProcessedEventStatus(eventWrapper.getEventId());
                        }
                    } catch (IOException ex) {
                    }
                });
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException ignored) {

        }
        return null;
    }

    public <T> String writeJsonString(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception ex) {
        }
        return StringUtils.EMPTY;
    }
}

