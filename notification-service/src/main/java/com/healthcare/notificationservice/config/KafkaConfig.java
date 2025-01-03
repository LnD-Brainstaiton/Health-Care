package com.healthcare.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "healthcare.kafka")
public class KafkaConfig {

    private String bootstrapServers;
    private Producer producer;
    private Consumer consumer;
    private Topic topic;

    // Getters and setters

    public static class Producer {
        private String compressionType;
        private String acks;
        private int batchSize;
        private int batchSizeBoostFactor;
        private int lingerMs;
        private int requestTimeoutMs;
        private int retryCount;

        // Getters and setters
    }

    public static class Consumer {
        private String groupId;
        private boolean batchListener;
        private boolean autoStartup;
        private int concurrencyLevel;
        private int sessionTimeoutMs;
        private int heartbeatIntervalMs;
        private int maxPollIntervalMs;
        private int pollTimeoutMs;
        private int maxPollRecords;
        private int maxPartitionFetchBytesDefault;
        private int maxPartitionFetchBytesBoostFactor;

        // Getters and setters
    }

    public static class Topic {
        private String dynamicNotification;

        // Getters and setters
    }
}
