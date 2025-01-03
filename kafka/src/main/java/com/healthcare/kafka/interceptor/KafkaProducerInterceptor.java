package com.healthcare.kafka.interceptor;

import com.healthcare.kafka.domain.EventWrapper;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class KafkaProducerInterceptor<T> implements ProducerInterceptor<String, EventWrapper<T>> {

    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";


    @Override
    public ProducerRecord<String, EventWrapper<T>> onSend(ProducerRecord<String, EventWrapper<T>> producerRecord) {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader(CURRENT_USER_CONTEXT_HEADER))
                .map(userContext -> createProducerRecordWithUserContext(producerRecord, userContext))
                .orElse(producerRecord);
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    private ProducerRecord<String, EventWrapper<T>> createProducerRecordWithUserContext(ProducerRecord<String, EventWrapper<T>> producerRecord, String userContext) {
        EventWrapper<T> eventWrapper = producerRecord.value();
        eventWrapper.setEventDate(LocalDateTime.now());
        eventWrapper.setUserContext(userContext);

        return new ProducerRecord<>(producerRecord.topic(), producerRecord.partition(), producerRecord.timestamp(), producerRecord.key(), eventWrapper);
    }



    @Override
    public void close() {
        // No operation necessary on close
    }

    @Override
    public void configure(Map<String, ?> map) {
        // No operation necessary on configure
    }
}
