package com.healthcare.kafka.config.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class holds the configuration properties related to Kafka producer.
 * These properties are read from the application.properties or kafka.yml file
 * and are used to configure how Kafka producers behave within the application.
 */
@Configuration
@ConfigurationProperties(prefix = "healthcare.kafka.producer")
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

    // Getters and Setters

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getBatchSizeBoostFactor() {
        return batchSizeBoostFactor;
    }

    public void setBatchSizeBoostFactor(Integer batchSizeBoostFactor) {
        this.batchSizeBoostFactor = batchSizeBoostFactor;
    }

    public Integer getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

    public Integer getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public void setRequestTimeoutMs(Integer requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
