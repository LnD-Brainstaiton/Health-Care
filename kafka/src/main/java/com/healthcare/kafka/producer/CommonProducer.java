package com.healthcare.kafka.producer;


import com.healthcare.kafka.domain.EventWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommonProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topicName, EventWrapper<Object> eventWrapper) {
        kafkaTemplate.send(topicName, eventWrapper);
    }

    @Async
    public void sendMessageAsync(String topicName, EventWrapper<Object> eventWrapper) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, eventWrapper);
    }
}