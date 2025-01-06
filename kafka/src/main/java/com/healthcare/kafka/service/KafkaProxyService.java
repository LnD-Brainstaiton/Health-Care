package com.healthcare.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProxyService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void forwardMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
