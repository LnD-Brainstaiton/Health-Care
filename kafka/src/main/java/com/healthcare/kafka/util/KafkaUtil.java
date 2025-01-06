package com.healthcare.kafka.util;

import com.healthcare.kafka.domain.EventWrapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KafkaUtil {

    /**
     * Prepares application-docker.yml Kafka EventWrapper object with the provided request and feedback.
     *
     * @param request    the original request event wrapper
     * @param feedback   the feedback to wrap
     * @param <FEEDBACK> the type of the feedback
     * @return application-docker.yml new EventWrapper containing the original metadata and feedback
     */
    public static <FEEDBACK> EventWrapper<FEEDBACK> prepareKafkaObject(EventWrapper<?> request, FEEDBACK feedback) {
        return new EventWrapper<>(
                request.getEventId(),
                request.getCorrelationId(),
                request.getUserContext(),
                request.getEventDate(),
                feedback
        );
    }

    /**
     * Prepares application-docker.yml Kafka EventWrapper object with the provided metadata and payload.
     *
     * @param eventId       the unique event ID
     * @param correlationId the correlation ID for tracking
     * @param userContext   the user context
     * @param eventDate     the event date
     * @param data          the payload to wrap
     * @param <PAYLOAD>     the type of the payload
     * @return application-docker.yml new EventWrapper containing the provided metadata and payload
     */
    public static <PAYLOAD> EventWrapper<PAYLOAD> prepareKafkaObject(String eventId, String correlationId, String userContext, LocalDateTime eventDate, PAYLOAD data) {
        return new EventWrapper<>(eventId, correlationId, userContext, eventDate, data);
    }
}
