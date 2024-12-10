package com.healthcare.kafka.config.data;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class holds the configuration properties related to Kafka producer.
 * These properties are read from the application.properties or application.yml file
 * and are used to configure how Kafka producers behave within the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "healthcare.kafka.producer")
// that start with "bs.kafka.producer" to this class.
public class KafkaProducerConfigData {

    // The compression type for the Kafka producer. Valid values include "none", "gzip", "snappy", etc.
    private String compressionType;

    // The acknowledgment level for producer requests. It determines how many brokers must acknowledge the message before
    // the producer considers the write successful. Values can be "0", "1", or "all".
    private String acks;

    // The batch size (in bytes) for Kafka producer. The producer will accumulate records in batches before sending them.
    private Integer batchSize;

    // The factor by which the batch size is boosted, allowing the producer to send larger batches if necessary.
    private Integer batchSizeBoostFactor;

    // The time (in milliseconds) the producer will wait before sending a batch of messages, even if the batch size is not met.
    private Integer lingerMs;

    // The timeout (in milliseconds) the producer will wait for a response from Kafka brokers before timing out.
    private Integer requestTimeoutMs;

    // The number of retries the Kafka producer should make in case of a failure when sending a message.
    private Integer retryCount;

}
