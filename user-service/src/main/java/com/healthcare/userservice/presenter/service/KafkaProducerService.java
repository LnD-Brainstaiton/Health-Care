package com.healthcare.userservice.presenter.service;

import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.kafka.producer.CommonProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final CommonProducer commonProducer;

    public Boolean publishEvent(String topic, Object data) {
        EventWrapper<Object> eventObject = new EventWrapper();
        eventObject.setEventId(getRandomUUID());
        eventObject.setCorrelationId(getRandomUUID());
        eventObject.setEventDate(LocalDateTime.now());
        eventObject.setData(data);

        commonProducer.sendMessageAsync(topic, eventObject);

        return true;
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
