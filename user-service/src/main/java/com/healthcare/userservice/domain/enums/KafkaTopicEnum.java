package com.healthcare.userservice.domain.enums;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public enum KafkaTopicEnum {

    DYNAMIC_NOTIFICATION("dynamicnotification"),;

    private String topic;

    KafkaTopicEnum(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    private void setTopic(final String topic) {
        this.topic = topic;
    }

    @Component
    public static class InitializeOutBoundTopic {
        @Value("${healthcare.kafka.topic.dynamic-notification}")
        private String dynamicNotification;

        @PostConstruct
        public void postConstruct() {
            DYNAMIC_NOTIFICATION.setTopic(dynamicNotification);
        }
    }
}

