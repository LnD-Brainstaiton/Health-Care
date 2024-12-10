package com.healthcare.kafka.config.data;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class holds the configuration properties related to Kafka consumer.
 * These properties are read from the application.properties or application.yml file 
 * and are used to configure how Kafka consumers behave within the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "healthcare.kafka.consumer")
public class KafkaConsumerConfigData {

    // The ID for the consumer group that Kafka uses to group consumers together for load balancing.
    private String groupId;

    // Determines if the Kafka consumer should operate in batch mode.
    private Boolean batchListener;

    // Determines if the consumer should automatically start consuming messages once initialized.
    private Boolean autoStartup;

    // Defines the concurrency level for the Kafka consumer (number of threads that will process the messages).
    private Integer concurrencyLevel;

    // The maximum time (in milliseconds) the consumer will wait for messages before considering a session expired.
    private Integer sessionTimeoutMs;

    // The interval (in milliseconds) at which the consumer will send heartbeat messages to the Kafka broker.
    private Integer heartbeatIntervalMs;

    // The maximum time (in milliseconds) the consumer will wait between poll requests to Kafka.
    private Integer maxPollIntervalMs;

    // The maximum amount of time (in milliseconds) the consumer will block while waiting for a poll response.
    private Long pollTimeoutMs;

    // The maximum number of records the consumer will fetch per poll.
    private Integer maxPollRecords;

    // The default maximum amount of data (in bytes) fetched from each Kafka partition during a poll request.
    private Integer maxPartitionFetchBytesDefault;

    // The multiplier that boosts the fetch size for Kafka partitions, useful for high-volume data consumption.
    private Integer maxPartitionFetchBytesBoostFactor;
}
